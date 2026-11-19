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