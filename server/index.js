const express = require('express');
const cors = require('cors');
const rateLimit = require('express-rate-limit');
const path = require('path');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3000;
const GEMINI_MODEL = process.env.GEMINI_MODEL || 'gemini-2.5-flash';

// Enable CORS for web client access
app.use(cors());

// Parse JSON request bodies
app.use(express.json());

// Apply rate limiting / basic abuse protection
const limiter = rateLimit({
  windowMs: 15 * 60 * 1000, // 15 minutes
  max: 100, // Limit each IP to 100 requests per 15 minutes
  message: { error: 'Too many requests from this IP, please try again after 15 minutes.' },
  standardHeaders: true,
  legacyHeaders: false
});
app.use('/api/', limiter);

// Serve static website files from the project's root `/public` directory
app.use(express.static(path.join(__dirname, '../public')));

// Secure Common AI Chat Endpoint
app.post('/api/ai/chat', async (req, res) => {
  const { message, profile, language, chatHistory } = req.body;

  // 1. Request Validation
  if (!message || typeof message !== 'string' || message.trim() === '') {
    return res.status(400).json({ error: 'Message is required and must be a non-empty string.' });
  }

  // 2. Validate API Key Presence
  const apiKey = process.env.GEMINI_API_KEY;
  if (!apiKey || apiKey.trim() === '' || apiKey.startsWith('MY_GEMINI_')) {
    console.error('CRITICAL: GEMINI_API_KEY environment variable is missing or unconfigured.');
    return res.status(500).json({ error: 'AI service is currently unconfigured on the server.' });
  }

  try {
    // 3. System Instructions Core ("Saksham Saathi" official government persona)
    const systemInstruction = `
You are 'Saksham Saathi' (सक्षम साथी), the official expert AI Financial & Business Advisor for the Saksham government portal under the Ministry of Social Justice & Empowerment & NSFDC (National Scheduled Castes Finance & Development Corporation), Government of India.

CRITICAL INSTRUCTIONS:
1. ANSWER SPECIFICALLY AND CONTEXTUALLY: 
   - Identify the exact core of the user's question. Answer that exact question clearly.
   - Understand conversation history. If the user refers to items discussed earlier, follow that thread.
   - If the user's business profile info is provided, customize your advice directly for their budget, location, and scale.
   - Provide actionable entrepreneur guidance (business ideas, dairy farm setups, cost structures, required documentation, steps to apply, etc.).
   - If the user asks in Hindi, Hinglish, Marathi, or other Indian languages, answer in the same language.
2. FACTUAL ACCURACY (DO NOT FABRICATE):
   - Rely on official NSFDC guidelines. Do not invent government schemes, interest rates, or channel partners.
3. FORMATTING RULES:
   - Use clean, bold headings with emojis.
   - Format details in short, scannable bullet points (•) and numbered steps (1., 2.).
   - Avoid huge walls of text. Be concise, friendly, professional, and practical.
   - Never start with "Based on your query..." or "Sure, here is...". Be direct and conversational.
4. SUGGESTED QUESTIONS (CRITICAL):
   - At the very end of your response, you MUST provide 3 to 4 suggested follow-up questions tailored to the current context.
   - Format them EXACTLY like this at the very bottom:
     SUGGESTIONS:
     - Your first question here
     - Your second question here
     - Your third question here
    `.trim();

    // 4. Construct API payload content structure
    const contents = [];

    // Formulate Context Prefixes (including business profiles and language specifications)
    let contextInfo = '';
    if (profile) {
      contextInfo = `
[SYSTEM NOTE: User Profile Data (Use this for context)]
• Business Focus: ${profile.businessType || 'Small Scale Enterprise'} (${profile.isExistingBusiness ? 'Existing' : 'New Greenfield Startup'})
• Location: ${profile.locationType || 'Rural'} area (${profile.district || 'Varanasi'}, ${profile.state || 'Uttar Pradesh'})
• Estimated Project Cost: ₹${profile.totalInvestment || 500000}
• Promoter Equity: ₹${profile.ownCapital || 50000} | Loan Required: ₹${profile.loanRequired || 450000}
• Annual Family Income: ${profile.annualFamilyIncome || 'Up to ₹3.00 Lakh'}
• Cattle Scale (if dairy): ${profile.dairyAnimalCount || 0} cows/buffaloes, ${profile.dairyLandAvailable || 'N/A'}
• Preferred Language: ${language || 'English'}
`.trim() + '\n\n';
    } else if (language) {
      contextInfo = `[SYSTEM NOTE: Preferred Language: ${language}]\n\n`;
    }

    // Map conversation history
    if (!chatHistory || chatHistory.length === 0) {
      contents.push({
        role: 'user',
        parts: [{ text: contextInfo + message }]
      });
    } else {
      chatHistory.forEach((msg, index) => {
        const role = msg.isUser ? 'user' : 'model';
        const text = index === 0 && msg.isUser ? contextInfo + msg.text : msg.text;
        contents.push({
          role: role,
          parts: [{ text: text }]
        });
      });
      // Append current user message
      contents.push({
        role: 'user',
        parts: [{ text: message }]
      });
    }

    const payload = {
      contents: contents,
      systemInstruction: {
        parts: [{ text: systemInstruction }]
      },
      generationConfig: {
        temperature: 0.6
      }
    };

    // 5. REST Call execution
    const url = `https://generativelanguage.googleapis.com/v1beta/models/${GEMINI_MODEL}:generateContent?key=${apiKey}`;

    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 35000); // 35 seconds timeout

    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload),
      signal: controller.signal
    });

    clearTimeout(timeoutId);

    if (!response.ok) {
      const errorText = await response.text();
      console.error(`Gemini API Response Error (HTTP ${response.status}):`, errorText);
      return res.status(502).json({ error: 'Failed to communicate with underlying AI service.' });
    }

    const responseData = await response.json();
    const candidates = responseData.candidates;
    if (candidates && candidates.length > 0) {
      const parts = candidates[0].content?.parts;
      if (parts && parts.length > 0) {
        let replyText = '';
        for (const part of parts) {
          if (part.text) replyText += part.text;
        }
        return res.json({ reply: replyText });
      }
    }

    return res.status(502).json({ error: 'AI engine generated an empty response.' });

  } catch (error) {
    if (error.name === 'AbortError') {
      console.error('Request Aborted: Contacting Gemini API timed out after 35 seconds.');
      return res.status(504).json({ error: 'AI response timed out. Please try again.' });
    }
    console.error('Error during backend communication with Gemini:', error);
    return res.status(500).json({ error: 'Internal Server Error while generating chat reply.' });
  }
});

// Start listening
app.listen(PORT, () => {
  console.log(`========================================`);
  console.log(` Saksham AI Gateway Backend Running!   `);
  console.log(` Port: ${PORT}                          `);
  console.log(` Model: ${GEMINI_MODEL}                 `);
  console.log(`========================================`);
});
