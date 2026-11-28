'use strict';

const S = { token: sessionStorage.getItem('token') };
let chatTimer = null;

const $main = document.getElementById('main');
const $nav = document.getElementById('nav');
const $toast = document.getElementById('toast');

function saveToken(t) {
  S.token = t;
  sessionStorage.setItem('token', t);
}

function clearToken() {
  S.token = null;
  sessionStorage.removeItem('token');
}

function stopChat() {
  clearInterval(chatTimer);
  chatTimer = null;
}

function toast(msg, bad) {
  $toast.textContent = msg;
  $toast.className = bad ? 'bad' : 'ok';
  clearTimeout(toast.t);
  toast.t = setTimeout(() => { $toast.className = 'hidden'; }, 3500);
}

function h(tag, attrs = {}, ...kids) {
  const el = document.createElement(tag);
  for (const [k, v] of Object.entries(attrs)) {
    if (k.startsWith('on')) el.addEventListener(k.slice(2), v);
    else if (k === 'class') el.className = v;
    else el.setAttribute(k, v);
  }
  for (const kid of kids.flat()) {
    if (kid != null && kid !== false) el.append(kid);
  }
  return el;
}

const safe = fn => async (...args) => {
  try {
    await fn(...args);
  } catch (e) {
    toast(e.message, true);
  }
};

const btn = (label, fn, cls = '') =>
  h('button', { class: cls, type: 'button', onclick: safe(fn) }, label);

async function api(method, path, body) {
  const headers = { 'Content-Type': 'application/json' };
  if (S.token) headers['Authorization'] = 'Bearer ' + S.token;

  const res = await fetch(path, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  });

  if (res.status === 401 && !path.startsWith('/api/auth/')) {
    clearToken();
    viewAuth('login');
    throw new Error('Session expire ho gaya, dobara login karo');
  }

  const text = await res.text();
  let data = null;
  try {
    data = text ? JSON.parse(text) : null;
  } catch (e) {
    // jawab JSON nahi tha
  }

  if (!res.ok) {
    const err = new Error((data && data.error) || 'Kuch gadbad ho gayi');
    err.status = res.status;
    throw err;
  }
  return data;
}

function viewAuth(mode = 'login', email = '') {
  stopChat();
  $nav.replaceChildren();

  const titles = { login: 'Login', register: 'Naya account banao', verify: 'OTP verify karo' };

  const emailIn = h('input', { type: 'email', placeholder: 'Email', maxlength: '120', value: email });
  const passIn = h('input', { type: 'password', placeholder: 'Password', maxlength: '72' });
  const otpIn = h('input', { placeholder: '6 digit OTP', maxlength: '6', inputmode: 'numeric' });

  const onSubmit = safe(async ev => {
    ev.preventDefault();

    if (mode === 'login') {
      const d = await api('POST', '/api/auth/login', { email: emailIn.value, password: passIn.value });
      saveToken(d.accessToken);
      viewHome();
    } else if (mode === 'register') {
      const d = await api('POST', '/api/auth/register', { email: emailIn.value, password: passIn.value });
      toast(d.message);
      viewAuth('verify', emailIn.value);
    } else {
      const d = await api('POST', '/api/auth/verify', { email: emailIn.value, otp: otpIn.value });
      toast(d.message);
      viewAuth('login', emailIn.value);
    }
  });

  const form = h('form', { class: 'card', onsubmit: onSubmit },
    h('h2', {}, titles[mode]),
    h('label', {}, 'Email', emailIn),
    mode !== 'verify' ? h('label', {}, 'Password', passIn) : null,
    mode === 'register' ? h('p', { class: 'muted' }, 'Kam se kam 8 character: capital letter, small letter aur number zaroori hai.') : null,
    mode === 'verify' ? h('label', {}, 'OTP', otpIn) : null,
    mode === 'verify' ? h('p', { class: 'muted' }, 'OTP abhi server ke terminal me dikhta hai.') : null,
    h('button', { type: 'submit' }, titles[mode])
  );

  const links = h('p', { class: 'muted' },
    mode !== 'login' ? btn('Login pe jao', () => viewAuth('login', emailIn.value), 'link') : null,
    mode !== 'register' ? btn('Naya account', () => viewAuth('register', emailIn.value), 'link') : null,
    mode !== 'verify' ? btn('OTP verify karna hai?', () => viewAuth('verify', emailIn.value), 'link') : null
  );

  $main.replaceChildren(form, links);
}

function setNav() {
  stopChat();
  $nav.replaceChildren(
    btn('Suggestions', viewSuggestions, 'link'),
    btn('Requests', viewRequests, 'link'),
    btn('Connections', viewConnections, 'link'),
    btn('Mera profile', viewProfile, 'link'),
    btn('Logout', logout, 'link')
  );
}

function logout() {
  clearToken();
  viewAuth('login');
}

function fmtHour(hr) {
  return (hr % 12 || 12) + ':00 ' + (hr < 12 ? 'AM' : 'PM');
}

const FIELDS = [
  ['name', 'Naam', 'text'],
  ['age', 'Age', 'number'],
  ['gender', 'Gender', 'select', ['MALE', 'FEMALE', 'OTHER']],
  ['occupation', 'Kaam (Job / Student)', 'text'],
  ['phone', 'Phone number (dusron ko nahi dikhega)', 'text'],
  ['city', 'City', 'text'],
  ['area', 'Area (jaise Marathahalli)', 'text'],
  ['budget', 'Monthly budget (Rs)', 'number'],
  ['sleepHour', 'Sone ka time (0-23, jaise 23 = raat 11 baje, 2 = raat 2 baje)', 'number'],
  ['wakeHour', 'Uthne ka time (0-23, jaise 7 = subah 7 baje)', 'number'],
  ['cleanliness', 'Safai (1 = relaxed, 5 = bahut clean)', 'number'],
  ['smoking', 'Main smoking karta/karti hoon', 'check'],
  ['drinking', 'Main drinking karta/karti hoon', 'check'],
  ['vegetarian', 'Main vegetarian hoon', 'check'],
  ['', 'Mujhe kaisa flatmate chahiye?', 'heading'],
  ['prefGender', 'Flatmate ka gender', 'select', ['ANY', 'MALE', 'FEMALE', 'OTHER']],
  ['prefMinAge', 'Flatmate ki minimum age', 'number'],
  ['prefMaxAge', 'Flatmate ki maximum age', 'number'],
  ['prefSmokingOk', 'Smoker chalega', 'check'],
  ['prefDrinkingOk', 'Drinker chalega', 'check'],
  ['prefVegOnly', 'Sirf vegetarian chahiye', 'check'],
  ['', 'Privacy', 'heading'],
  ['visible', 'Mera profile dusron ko search me dikhe', 'check'],
];

async function viewProfile() {
  setNav();

  let p = {};
  let first = false;
  try {
    p = await api('GET', '/api/profile/me');
  } catch (e) {
    if (e.status !== 404) throw e;
    first = true;
  }
  p = {
    gender: 'MALE', sleepHour: 23, wakeHour: 7, cleanliness: 3,
    prefGender: 'ANY', prefMinAge: 18, prefMaxAge: 60, visible: true,
    ...p,
  };

  const inputs = {};
  const rows = FIELDS.map(([key, label, type, options]) => {
    if (type === 'heading') return h('h3', {}, label);

    let input;
    if (type === 'select') {
      input = h('select', {}, options.map(o => h('option', { value: o }, o)));
    } else if (type === 'check') {
      input = h('input', { type: 'checkbox' });
    } else {
      input = h('input', { type, maxlength: '60' });
    }

    if (type === 'check') input.checked = !!p[key];
    else input.value = p[key] ?? '';

    inputs[key] = input;
    return type === 'check'
      ? h('label', { class: 'row' }, input, label)
      : h('label', {}, label, input);
  });

  const form = h('form', {
      class: 'card',
      onsubmit: safe(async ev => {
        ev.preventDefault();
        const body = {};
        for (const [key, , type] of FIELDS) {
          if (type === 'heading') continue;
          const el = inputs[key];
          body[key] = type === 'check' ? el.checked
            : type === 'number' ? Number(el.value)
            : el.value.trim();
        }
        await api('PUT', '/api/profile/me', body);
        toast('Profile save ho gaya');
      }),
    },
    h('h2', {}, first ? 'Pehle apna profile banao' : 'Mera profile'),
    rows,
    h('button', { type: 'submit' }, 'Profile save karo')
  );

  $main.replaceChildren(form);
}

async function viewSuggestions() {
  setNav();

  let list;
  try {
    list = await api('GET', '/api/suggestions');
  } catch (e) {
    if (e.status === 400) {
      toast(e.message, true);
      return viewProfile();
    }
    throw e;
  }

  const head = h('h2', {}, 'Tumhare liye flatmates');
  if (!list.length) {
    $main.replaceChildren(head,
      h('p', { class: 'card muted' }, 'Abhi koi match nahi mila. Profile me area, budget ya pasand thodi badal ke dekho.'));
    return;
  }

  $main.replaceChildren(head, ...list.map(s => {
    const card = h('div', { class: 'card' },
      h('span', { class: 'badge' }, s.score + '% match'),
      h('h3', {}, s.name + ', ' + s.age),
      h('p', {}, s.occupation + ' | ' + s.area + ' | Budget Rs ' + s.budget),
      h('p', {}, 'Sota: ' + fmtHour(s.sleepHour) + ' | Uthta: ' + fmtHour(s.wakeHour) + ' | Safai: ' + s.cleanliness + '/5'),
      h('div', { class: 'tags' },
        h('span', {}, s.smoking ? 'Smoker' : 'Non-smoker'),
        h('span', {}, s.drinking ? 'Drinks' : 'No drinking'),
        h('span', {}, s.vegetarian ? 'Veg' : 'Non-veg')),
      btn('Request bhejo', async () => {
        const d = await api('POST', '/api/requests/send/' + s.userId);
        toast(d.message);
        card.remove();
      })
    );
    return card;
  }));
}

async function viewRequests() {
  setNav();

  const list = await api('GET', '/api/requests/incoming');
  const head = h('h2', {}, 'Aayi hui requests');

  if (!list.length) {
    $main.replaceChildren(head, h('p', { class: 'card muted' }, 'Abhi koi nayi request nahi hai.'));
    return;
  }

  $main.replaceChildren(head, ...list.map(r => {
    const card = h('div', { class: 'card' },
      h('span', { class: 'badge' }, r.score + '% match'),
      h('h3', {}, r.name + ', ' + r.age),
      h('p', {}, r.area + ' | Budget Rs ' + r.budget),
      btn('Accept', async () => {
        const d = await api('POST', '/api/requests/' + r.id + '/accept');
        toast(d.message);
        card.remove();
      }),
      btn('Reject', async () => {
        const d = await api('POST', '/api/requests/' + r.id + '/reject');
        toast(d.message);
        card.remove();
      }, 'danger')
    );
    return card;
  }));
}

async function viewConnections() {
  setNav();

  const list = await api('GET', '/api/connections');
  const head = h('h2', {}, 'Tumhare connections');

  if (!list.length) {
    $main.replaceChildren(head, h('p', { class: 'card muted' }, 'Abhi koi connection nahi. Request accept hone ke baad yahan dikhega.'));
    return;
  }

  $main.replaceChildren(head, ...list.map(c =>
    h('div', { class: 'card' },
      h('h3', {}, c.name),
      h('p', {}, 'Area: ' + c.area),
      h('p', {}, 'Phone: ' + c.phone),
      btn('Chat karo', () => viewChat(c.userId, c.name))
    )
  ));
}

async function viewChat(userId, name) {
  setNav();

  const box = h('div', { class: 'chat-box' });
  const input = h('input', { maxlength: '500', placeholder: 'Message likho...' });

  async function load() {
    const msgs = await api('GET', '/api/chat/' + userId);
    box.replaceChildren(...msgs.map(m =>
      h('div', { class: 'msg' + (m.fromMe ? ' me' : '') }, m.text)));
    box.scrollTop = box.scrollHeight;
  }

  const form = h('form', {
      class: 'chat-form',
      onsubmit: safe(async ev => {
        ev.preventDefault();
        if (!input.value.trim()) return;
        await api('POST', '/api/chat/' + userId, { text: input.value });
        input.value = '';
        await load();
      }),
    },
    input,
    h('button', { type: 'submit' }, 'Bhejo')
  );

  $main.replaceChildren(
    h('h2', {}, 'Chat: ' + name),
    box,
    form,
    btn('Wapas', viewConnections, 'link')
  );

  await load();
  chatTimer = setInterval(() => { load().catch(stopChat); }, 4000);
}

function viewHome() {
  safe(viewSuggestions)();
}

if (S.token) viewHome();
else viewAuth('login');
