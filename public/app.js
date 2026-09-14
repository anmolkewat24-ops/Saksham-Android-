// Web UI Controller for Saksham Saathi AI Advisor

document.addEventListener('DOMContentLoaded', () => {
    const chatForm = document.getElementById('chat-form');
    const chatInput = document.getElementById('chat-input');
    const sendBtn = document.getElementById('send-btn');
    const chatStream = document.getElementById('chat-stream');
    const clearChatBtn = document.getElementById('clear-chat-btn');
    const langSelector = document.getElementById('lang-selector');
    const topicChips = document.querySelectorAll('.topic-chip');

    // Chat history memory
    let chatHistory = [];
    let isThinking = false;
    let selectedLanguage = "English";

    // Dynamic Language Map for Web Elements
    const langMap = {
        English: {
            title: "Saksham Saathi (सक्षम साथी)",
            subtitle: "Official Concessional Scheme & Business Guide",
            placeholder: "Type about loans, cattle, rates, documents...",
            welcomeName: "Saksham Saathi • Official Advisor",
            welcomeTxt: `<p>🇮🇳 <strong>Namaste!</strong> I am your AI assistant, specialized in government financial schemes under the Ministry of Social Justice and NSFDC.</p>
                         <p class="mt-2">I can help you understand <strong>4% Mahila Samriddhi loans</strong>, calculate <strong>moratorium gestational EMIs</strong>, explain required <strong>document check dockets</strong>, and plan your business setup. How can I guide you today?</p>`
        },
        Hindi: {
            title: "सक्षम साथी (Saksham Saathi)",
            subtitle: "आधिकारिक रियायती योजना और व्यवसाय सलाहकार",
            placeholder: "ऋण, मवेशी, ब्याज दर और दस्तावेजों के बारे में लिखें...",
            welcomeName: "सक्षम साथी • आधिकारिक सलाहकार",
            welcomeTxt: `<p>🇮🇳 <strong>नमस्ते!</strong> मैं आपका सक्षम साथी एआई सहायक हूँ। मैं सामाजिक न्याय और अधिकारिता मंत्रालय एवं NSFDC के तहत सरकारी वित्तीय योजनाओं का विशेषज्ञ हूँ।</p>
                         <p class="mt-2">मैं आपको <strong>4% महिला समृद्धि ऋण</strong> समझने, <strong>मोराटोरियम अवधि ईएमआई</strong> की गणना करने, आवश्यक <strong>दस्तावेजों की सूची</strong> समझने और आपके व्यवसाय सेटअप की योजना बनाने में सहायता कर सकता हूँ। आज मैं आपका मार्गदर्शन कैसे करूँ?</p>`
        },
        Marathi: {
            title: "सक्षम साथी (Saksham Saathi)",
            subtitle: "अधिकृत सवलत योजना आणि व्यवसाय सल्लागार",
            placeholder: "कर्ज, गुरेढोरे, व्याजदर आणि कागदपत्रांबद्दल विचार करा...",
            welcomeName: "सक्षम साथी • अधिकृत सल्लागार",
            welcomeTxt: `<p>🇮🇳 <strong>नमस्कार!</strong> मी तुमचा सक्षम साथी एआय सहाय्यक आहे. मी सामाजिक न्याय आणि सक्षमीकरण मंत्रालय आणि NSFDC च्या अंतर्गत सरकारी सवलत योजनांचा तज्ज्ञ आहे.</p>
                         <p class="mt-2">मी तुम्हाला <strong>४% महिला समृद्धी कर्ज</strong> समजून घेण्यास, <strong>मोरेटोरियम कालावधी ईएमआई</strong> मोजण्यास, आवश्यक <strong>कागदपत्रांची चेकलिस्ट</strong> आणि व्यवसाय सुरू करण्याच्या नियोजनात मदत करू शकतो. आज मी तुम्हाला काय मदत करू?</p>`
        },
        Bengali: {
            title: "সক্ষম সাথী (Saksham Saathi)",
            subtitle: "সরকারি সুবিধাজনক ঋণ ও ব্যবসা উপদেষ্টা",
            placeholder: "ঋণ, গবাদি পশু, সুদের হার এবং নথি সম্পর্কে লিখুন...",
            welcomeName: "সক্ষম সাথী • অফিসিয়াল উপদেষ্টা",
            welcomeTxt: `<p>🇮🇳 <strong>নমস্কার!</strong> আমি আপনার সক্ষম সাথী এআই সহকারী। আমি সামাজিক ন্যায় ও ক্ষমতায়ন মন্ত্রক এবং NSFDC এর অধীনস্থ সরকারি আর্থিক প্রকল্পগুলির বিশেষজ্ঞ।</p>
                         <p class="mt-2">আমি আপনাকে <strong>৪% মহিলা সমৃদ্ধি ঋণ</strong> বুঝতে, <strong>মোরাটোরিয়াম কিস্তি হিসাব</strong> করতে, প্রয়োজনীয় <strong>নথিপত্রের তালিকা</strong> জানতে এবং আপনার ব্যবসা শুরু করার পরিকল্পনা করতে সাহায্য করতে পারি। আজ আমি আপনাকে কীভাবে সাহায্য করব?</p>`
        }
    };

    // Enable/disable send button dynamically based on text length
    chatInput.addEventListener('input', () => {
        sendBtn.disabled = chatInput.value.trim() === '' || isThinking;
    });

    // Handle Language Switch
    langSelector.addEventListener('change', (e) => {
        selectedLanguage = e.target.value;
        const layout = langMap[selectedLanguage] || langMap.English;

        document.getElementById('advisor-title').textContent = layout.title;
        document.getElementById('advisor-subtitle').textContent = layout.subtitle;
        chatInput.placeholder = layout.placeholder;

        // Reset conversation with language-specific greeting if history is empty
        if (chatHistory.length === 0) {
            chatStream.innerHTML = `
                <div class="flex items-start space-x-2.5">
                    <div class="bg-[#1E3A8A] text-white text-[10px] w-7 h-7 rounded-full flex items-center justify-center font-bold">SS</div>
                    <div class="flex flex-col max-w-[85%]">
                        <span class="text-[10px] font-bold text-slate-500 mb-0.5">${layout.welcomeName}</span>
                        <div class="bg-white p-3 rounded-2xl rounded-tl-sm border border-slate-200 text-xs shadow-sm leading-relaxed text-slate-800">
                            ${layout.welcomeTxt}
                        </div>
                    </div>
                </div>
            `;
        }
    });

    // Clear Chat Logic
    clearChatBtn.addEventListener('click', () => {
        if (confirm("Are you sure you want to clear this conversation history?")) {
            chatHistory = [];
            chatStream.innerHTML = '';
            // Trigger language selection change to restore language welcome message
            langSelector.dispatchEvent(new Event('change'));
        }
    });

    // Handle Quick Topic Clicks
    topicChips.forEach(chip => {
        chip.addEventListener('click', () => {
            const query = chip.getAttribute('data-prompt');
            if (query && !isThinking) {
                chatInput.value = query;
                chatForm.dispatchEvent(new Event('submit'));
            }
        });
    });

    // Main Submit Action
    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const rawMessageText = chatInput.value.trim();
        if (rawMessageText === '' || isThinking) return;

        // 1. Clear input and disable send
        chatInput.value = '';
        sendBtn.disabled = true;
        isThinking = true;

        // 2. Append User Bubble
        appendMessageBubble(rawMessageText, true);
        scrollToBottom();

        // 3. Show typing loading state
        const loadingId = appendTypingIndicator();
        scrollToBottom();

        // 4. Fire HTTPS REST Call
        try {
            const apiResponse = await executeChatRequestWithRetry(rawMessageText);
            removeTypingIndicator(loadingId);

            // 5. Success Flow
            const botReplyText = apiResponse.reply;

            // Separate dynamic suggestions
            let cleanText = botReplyText;
            let suggestions = [];
            const suggestionsIndex = botReplyText.indexOf('SUGGESTIONS:');

            if (suggestionsIndex !== -1) {
                cleanText = botReplyText.substring(0, suggestionsIndex).trim();
                const rawSuggestions = botReplyText.substring(suggestionsIndex + 'SUGGESTIONS:'.length).trim();
                suggestions = rawSuggestions.split('\n')
                    .map(item => item.trim().replace(/^[-•*]\s*/, '').trim())
                    .filter(item => item !== '');
            }

            // Append bot response and add memory history
            appendMessageBubble(cleanText, false, suggestions);
            chatHistory.push({ isUser: true, text: rawMessageText });
            chatHistory.push({ isUser: false, text: cleanText });

        } catch (error) {
            console.error('Fetch execution error:', error);
            removeTypingIndicator(loadingId);
            // Append Error Bubble with retry functionality
            appendErrorBubble(rawMessageText, error.message || 'Unable to connect to AI gateway.');
        } finally {
            isThinking = false;
            sendBtn.disabled = chatInput.value.trim() === '';
            scrollToBottom();
        }
    });

    // Execute API Post with automated connection retries
    async function executeChatRequestWithRetry(messageText, retries = 2) {
        const payload = {
            message: messageText,
            language: selectedLanguage,
            chatHistory: chatHistory
        };

        for (let i = 0; i <= retries; i++) {
            try {
                const response = await fetch('/api/ai/chat', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(payload)
                });

                if (response.status === 429) {
                    throw new Error('Rate limit exceeded (Too many queries). Please wait a few minutes.');
                }

                if (!response.ok) {
                    const errData = await response.json().catch(() => ({}));
                    throw new Error(errData.error || `HTTP error ${response.status}`);
                }

                return await response.json();
            } catch (err) {
                if (i === retries) throw err;
                console.warn(`Connection attempt ${i + 1} failed. Retrying...`);
                await new Promise(resolve => setTimeout(resolve, 1000));
            }
        }
    }

    // Dynamic Bubble Appender
    function appendMessageBubble(text, isUser, suggestions = []) {
        const bubbleWrapper = document.createElement('div');
        bubbleWrapper.className = `flex items-start space-x-2.5 ${isUser ? 'justify-end' : ''}`;

        const avatar = isUser 
            ? `<div class="bg-slate-300 text-slate-700 text-[10px] w-7 h-7 rounded-full flex items-center justify-center font-bold order-2 ml-2.5">ME</div>`
            : `<div class="bg-[#1E3A8A] text-white text-[10px] w-7 h-7 rounded-full flex items-center justify-center font-bold">SS</div>`;

        const titleText = isUser ? 'You' : (langMap[selectedLanguage]?.welcomeName || langMap.English.welcomeName);
        const alignments = isUser ? 'items-end' : '';
        const bodyColors = isUser ? 'bg-blue-600 text-white rounded-tr-sm' : 'bg-white text-slate-800 rounded-tl-sm border border-slate-200';

        const parsedContent = isUser ? escapeHtml(text) : parseMarkdown(text);

        let suggestionsHtml = '';
        if (suggestions.length > 0) {
            suggestionsHtml = `
                <div class="mt-2.5 flex flex-col space-y-1.5 w-full">
                    ${suggestions.map(s => `
                        <button class="suggestion-btn text-left bg-[#EFF6FF] border border-blue-200 hover:bg-blue-100 text-blue-800 text-[11px] font-semibold px-3 py-1.5 rounded-xl transition duration-200 flex items-center" data-prompt="${escapeAttribute(s)}">
                            <span class="text-orange-500 mr-1.5 font-bold">➔</span> ${escapeHtml(s)}
                        </button>
                    `).join('')}
                </div>
            `;
        }

        bubbleWrapper.innerHTML = `
            ${avatar}
            <div class="flex flex-col max-w-[85%] ${alignments}">
                <span class="text-[10px] font-bold text-slate-500 mb-0.5">${titleText}</span>
                <div class="p-3 rounded-2xl text-xs shadow-sm leading-relaxed formatted-response ${bodyColors}">
                    ${parsedContent}
                </div>
                ${suggestionsHtml}
            </div>
        `;

        chatStream.appendChild(bubbleWrapper);

        // Map Click events on Suggestions
        if (suggestions.length > 0) {
            bubbleWrapper.querySelectorAll('.suggestion-btn').forEach(btn => {
                btn.addEventListener('click', () => {
                    const prompt = btn.getAttribute('data-prompt');
                    if (prompt && !isThinking) {
                        chatInput.value = prompt;
                        chatForm.dispatchEvent(new Event('submit'));
                    }
                });
            });
        }
    }

    // Append Typing Indicator Animation
    function appendTypingIndicator() {
        const id = 'loading_' + Date.now();
        const wrapper = document.createElement('div');
        wrapper.id = id;
        wrapper.className = 'flex items-start space-x-2.5';

        wrapper.innerHTML = `
            <div class="bg-[#1E3A8A] text-white text-[10px] w-7 h-7 rounded-full flex items-center justify-center font-bold">SS</div>
            <div class="flex flex-col max-w-[85%]">
                <span class="text-[10px] font-bold text-slate-500 mb-0.5">Saksham Saathi • Thinking</span>
                <div class="bg-white border border-slate-200 px-4 py-3 rounded-2xl rounded-tl-sm text-xs shadow-sm flex items-center text-slate-500">
                    <span class="mr-2" id="analyzing-text">${selectedLanguage === 'Hindi' ? 'सक्षम साथी विश्लेषण कर रहा है' : 'Analyzing details'}</span>
                    <div class="flex space-x-1">
                        <span class="w-1.5 h-1.5 bg-[#1E3A8A] rounded-full dot-1"></span>
                        <span class="w-1.5 h-1.5 bg-[#1E3A8A] rounded-full dot-2"></span>
                        <span class="w-1.5 h-1.5 bg-[#1E3A8A] rounded-full dot-3"></span>
                    </div>
                </div>
            </div>
        `;

        chatStream.appendChild(wrapper);
        return id;
    }

    function removeTypingIndicator(id) {
        const indicator = document.getElementById(id);
        if (indicator) indicator.remove();
    }

    // Append Error Bubble with live Retry link
    function appendErrorBubble(retryPromptText, errorMessage) {
        const bubbleWrapper = document.createElement('div');
        bubbleWrapper.className = 'flex items-start space-x-2.5';

        bubbleWrapper.innerHTML = `
            <div class="bg-red-600 text-white text-[10px] w-7 h-7 rounded-full flex items-center justify-center font-bold">!</div>
            <div class="flex flex-col max-w-[85%]">
                <span class="text-[10px] font-bold text-red-500 mb-0.5">Connection Interruption</span>
                <div class="bg-red-50 border border-red-200 text-red-800 p-3 rounded-2xl rounded-tl-sm text-xs shadow-sm leading-relaxed">
                    <p class="font-semibold"><i class="fa-solid fa-triangle-exclamation mr-1.5"></i> ${escapeHtml(errorMessage)}</p>
                    <button class="retry-btn mt-2.5 bg-red-100 hover:bg-red-200 text-red-900 border border-red-300 font-bold px-3 py-1 rounded-lg transition duration-150 flex items-center text-[10px]">
                        <i class="fa-solid fa-arrows-rotate mr-1.5 animate-spin-reverse"></i> Retry Query
                    </button>
                </div>
            </div>
        `;

        chatStream.appendChild(bubbleWrapper);

        // Hook retry button
        bubbleWrapper.querySelector('.retry-btn').addEventListener('click', () => {
            bubbleWrapper.remove();
            chatInput.value = retryPromptText;
            chatForm.dispatchEvent(new Event('submit'));
        });
    }

    // Helper functions for escaping HTML and safety
    function escapeHtml(unsafe) {
        return unsafe
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    function escapeAttribute(unsafe) {
        return unsafe.replace(/"/g, '&quot;').replace(/'/g, '&#39;');
    }

    function scrollToBottom() {
        chatStream.scrollTop = chatStream.scrollHeight;
    }

    // Basic markdown/bullet parser for response beauty
    function parseMarkdown(rawText) {
        const lines = rawText.split('\n');
        let html = '';
        let listOpen = false;
        let listType = ''; // 'ul' or 'ol'

        lines.forEach(line => {
            const trimmed = line.trim();

            if (trimmed === '') {
                if (listOpen) {
                    html += `</${listType}>`;
                    listOpen = false;
                    listType = '';
                }
                html += '<div class="h-2"></div>';
                return;
            }

            if (trimmed === '---' || trimmed === '***') {
                if (listOpen) {
                    html += `</${listType}>`;
                    listOpen = false;
                    listType = '';
                }
                html += '<hr class="border-slate-200 my-2">';
                return;
            }

            // Headings starts with ** or #
            const startsWithDoubleStar = trimmed.startsWith('**') && trimmed.endsWith('**');
            const startsWithHash = trimmed.startsWith('#');

            if (startsWithDoubleStar || startsWithHash) {
                if (listOpen) {
                    html += `</${listType}>`;
                    listOpen = false;
                    listType = '';
                }

                let headingText = trimmed;
                if (startsWithDoubleStar) {
                    headingText = trimmed.substring(2, trimmed.length - 2);
                } else {
                    headingText = trimmed.replace(/^#+\s*/, '');
                }

                html += `<h3>${escapeHtml(headingText)}</h3>`;
                return;
            }

            // Callout box with 💡
            if (trimmed.startsWith('💡') || trimmed.startsWith('Tip:') || trimmed.startsWith('सलाह:')) {
                if (listOpen) {
                    html += `</${listType}>`;
                    listOpen = false;
                    listType = '';
                }
                html += `<div class="callout-box">${escapeHtml(trimmed.replace(/\*\*/g, ''))}</div>`;
                return;
            }

            // Unordered list bullets (starts with •, -, *)
            const isBullet = trimmed.startsWith('•') || trimmed.startsWith('-') || trimmed.startsWith('* ');
            // Numbered list (starts with digit followed by dot)
            const isNumbered = /^\d+\./.test(trimmed);

            if (isBullet) {
                if (!listOpen || listType !== 'ul') {
                    if (listOpen) html += `</${listType}>`;
                    html += '<ul class="space-y-1 list-disc pl-5 mt-1">';
                    listOpen = true;
                    listType = 'ul';
                }
                const content = trimmed.replace(/^[-•*]\s*/, '').replace(/\*\*/g, '');
                html += `<li>${escapeHtml(content)}</li>`;
                return;
            }

            if (isNumbered) {
                if (!listOpen || listType !== 'ol') {
                    if (listOpen) html += `</${listType}>`;
                    html += '<ol class="space-y-1 list-decimal pl-5 mt-1">';
                    listOpen = true;
                    listType = 'ol';
                }
                const content = trimmed.replace(/^\d+\.\s*/, '').replace(/\*\*/g, '');
                html += `<li>${escapeHtml(content)}</li>`;
                return;
            }

            // Normal plain text line
            if (listOpen) {
                html += `</${listType}>`;
                listOpen = false;
                listType = '';
            }
            html += `<p>${escapeHtml(trimmed.replace(/\*\*/g, ''))}</p>`;
        });

        if (listOpen) {
            html += `</${listType}>`;
        }

        return html;
    }
});
