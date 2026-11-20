'use strict';

const S = { token: sessionStorage.getItem('token') };

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
function viewAuth(mode = 'login', email = '') {
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

  const form = h('form', { onsubmit: onSubmit },
    h('h2', {}, titles[mode]),
    emailIn,
    mode === 'verify' ? otpIn : passIn,
    h('button', { type: 'submit' }, mode === 'verify' ? 'Verify' : mode === 'login' ? 'Login' : 'Register')
  );
  $main.replaceChildren(h('div', { class: 'card' }, form));
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
viewAuth('login');
  const form = h('form', { class: 'card', onsubmit: onSubmit },
    h('h2', {}, titles[mode]),
    h('label', {}, 'Email', emailIn),
    mode !== 'verify' ? h('label', {}, 'Password', passIn) : null,
    mode === 'register' ? h('p', { class: 'muted' }, 'Kam se kam 8 character: capital letter, small letter aur number zaroori hai.') : null,
    mode === 'verify' ? h('label', {}, 'OTP', otpIn) : null,
    mode === 'verify' ? h('p', { class: 'muted' }, 'OTP abhi server ke terminal me dikhta hai.') : null,
    h('button', { type: 'submit' }, titles[mode])
  );