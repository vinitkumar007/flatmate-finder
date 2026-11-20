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
$main.replaceChildren(
  h('div', { class: 'card' },
    h('h2', {}, 'JS chal rahi hai'),
    btn('Toast dikhao', () => toast('Namaste!'))
  )
);