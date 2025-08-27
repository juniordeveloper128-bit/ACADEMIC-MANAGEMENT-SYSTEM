let player;

function onYouTubeIframeAPIReady() {
  player = new YT.Player('player', {
    height: '100%',
    width: '100%',
    videoId: '',
    playerVars: { autoplay: 0, controls: 1 },
    events: {}
  });
}

async function translateLines(lines, target, source) {
  const res = await fetch('/api/translate', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ lines, targetLanguage: target, sourceLanguage: source || null })
  });
  if (!res.ok) throw new Error('Translation failed');
  const data = await res.json();
  return data.translated || lines;
}

function renderCaptions(lines, container) {
  container.innerHTML = '';
  lines.forEach((l) => {
    const el = document.createElement('div');
    el.className = 'caption-line';
    el.innerHTML = `<div class="caption-time">00:00</div><div>${l}</div>`;
    container.appendChild(el);
  });
}

window.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('video-form');
  const videoIdInput = document.getElementById('videoId');
  const targetInput = document.getElementById('targetLang');
  const sourceInput = document.getElementById('sourceLang');
  const captionsEl = document.getElementById('captions');

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const videoId = videoIdInput.value.trim();
    const target = targetInput.value.trim();
    const source = (sourceInput.value || '').trim();
    if (!videoId || !target) return;

    if (player && player.loadVideoById) {
      player.loadVideoById(videoId);
    }

    const demoLines = [
      'Welcome to the stream, thanks for joining!',
      'In this session we will explore Spring Boot basics.',
      'Feel free to ask questions in the chat.',
      'Now let\'s implement a REST API and connect a frontend.',
    ];

    captionsEl.innerHTML = '<div class="caption-line">Translating…</div>';
    try {
      const translated = await translateLines(demoLines, target, source);
      renderCaptions(translated, captionsEl);
    } catch (err) {
      renderCaptions(demoLines, captionsEl);
    }
  });
});

