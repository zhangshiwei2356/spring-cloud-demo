/**
 * 演示站公共脚本：会话、API 请求。
 */
const AppSession = {
    TOKEN_KEY: 'demo_token',
    USER_KEY: 'demo_user',

    getToken() {
        return sessionStorage.getItem(this.TOKEN_KEY) || '';
    },

    getUser() {
        try {
            return JSON.parse(sessionStorage.getItem(this.USER_KEY) || 'null');
        } catch (e) {
            return null;
        }
    },

    save(token, user) {
        sessionStorage.setItem(this.TOKEN_KEY, token);
        sessionStorage.setItem(this.USER_KEY, JSON.stringify(user));
    },

    clear() {
        sessionStorage.removeItem(this.TOKEN_KEY);
        sessionStorage.removeItem(this.USER_KEY);
    },

    isLoggedIn() {
        return !!this.getToken() && !!this.getUser();
    },

    requireAuth() {
        if (!this.isLoggedIn()) {
            window.location.replace('/login.html');
            return false;
        }
        return true;
    },

    redirectIfLoggedIn(target) {
        if (this.isLoggedIn()) {
            window.location.replace(target || '/admin.html');
        }
    }
};

async function apiRequest(method, path, body, needAuth) {
    const headers = {
        'Content-Type': 'application/json',
        'X-Trace-Id': 'demo-' + Date.now()
    };
    if (needAuth) {
        const token = AppSession.getToken();
        if (!token) {
            AppSession.clear();
            window.location.replace('/login.html');
            throw new Error('未登录或登录已过期，请重新登录');
        }
        headers['Authorization'] = 'Bearer ' + token;
    }
    const opts = { method, headers };
    if (body != null) {
        opts.body = JSON.stringify(body);
    }
    const res = await fetch(path, opts);
    const text = await res.text();
    let json;
    try {
        json = JSON.parse(text);
    } catch (e) {
        throw new Error('HTTP ' + res.status + ': ' + text);
    }
    if (!res.ok && json.code == null) {
        json = { code: res.status, message: text, data: null };
    }
    if (needAuth && (json.code === 401 || res.status === 401)) {
        AppSession.clear();
        window.location.replace('/login.html');
        throw new Error(json.message || '登录已过期，请重新登录');
    }
    return json;
}

async function login(userId, password) {
    const res = await apiRequest('POST', '/cloud-auth/api/auth/login', { userId, password }, false);
    if (res.code !== 200 || !res.data || !res.data.accessToken) {
        throw new Error(res.message || '登录失败');
    }
    const d = res.data;
    const user = {
        userId: d.userId,
        userName: d.userName,
        role: d.role,
        avatarUrl: d.avatarUrl,
        expireInSeconds: d.expireInSeconds
    };
    AppSession.save(d.accessToken, user);
    return user;
}

function logout() {
    AppSession.clear();
    window.location.replace('/login.html');
}

function renderJson(el, data, isError) {
    if (!el) return;
    el.textContent = typeof data === 'string' ? data : JSON.stringify(data, null, 2);
    el.style.borderColor = isError ? 'var(--err)' : 'var(--border)';
}

function showAlert(el, msg, isError) {
    if (!el) return;
    el.textContent = msg;
    el.className = 'alert show' + (isError ? ' err' : '');
}
