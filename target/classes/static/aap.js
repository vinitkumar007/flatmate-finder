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