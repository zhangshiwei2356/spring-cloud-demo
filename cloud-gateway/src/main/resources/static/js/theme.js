/**
 * 主题切换：localStorage 键 sc_demo_theme，写入 html[data-theme]。
 */
const AppTheme = (function () {
    const STORAGE_KEY = 'sc_demo_theme';
    const DEFAULT = 'midnight';

    const THEMES = [
        { id: 'midnight', label: '深夜蓝', swatch: ['#0b1220', '#3b82f6'] },
        { id: 'ocean', label: '海洋青', swatch: ['#071a24', '#06b6d4'] },
        { id: 'forest', label: '森林绿', swatch: ['#0a1510', '#22c55e'] },
        { id: 'sunset', label: '暮光紫', swatch: ['#1a1018', '#f472b6'] },
        { id: 'amber', label: '琥珀金', swatch: ['#14100a', '#f59e0b'] },
        { id: 'light', label: '浅色', swatch: ['#f1f5f9', '#2563eb'] }
    ];

    function get() {
        try {
            const saved = localStorage.getItem(STORAGE_KEY);
            if (saved && THEMES.some((t) => t.id === saved)) {
                return saved;
            }
        } catch (e) { /* ignore */ }
        return DEFAULT;
    }

    function apply(themeId) {
        const id = THEMES.some((t) => t.id === themeId) ? themeId : DEFAULT;
        document.documentElement.setAttribute('data-theme', id);
        try {
            localStorage.setItem(STORAGE_KEY, id);
        } catch (e) { /* ignore */ }
        document.querySelectorAll('[data-theme-option]').forEach(function (btn) {
            const active = btn.getAttribute('data-theme-option') === id;
            btn.classList.toggle('is-active', active);
            btn.setAttribute('aria-pressed', active ? 'true' : 'false');
        });
        const labelEl = document.querySelector('[data-theme-current-label]');
        if (labelEl) {
            const t = THEMES.find((x) => x.id === id);
            labelEl.textContent = t ? t.label : id;
        }
        return id;
    }

    function init() {
        apply(get());
    }

    function mountPicker(root) {
        if (!root) return;
        const current = get();
        const currentTheme = THEMES.find((t) => t.id === current);
        let html = '<div class="theme-switcher">';
        html += '<button type="button" class="btn btn-ghost btn-sm theme-trigger" data-theme-trigger aria-haspopup="true" aria-expanded="false">';
        html += '<span class="theme-trigger-icon" aria-hidden="true">◐</span>';
        html += '<span data-theme-current-label">' + (currentTheme ? currentTheme.label : '主题') + '</span>';
        html += '</button>';
        html += '<div class="theme-panel" data-theme-panel hidden>';
        html += '<p class="theme-panel-title">主题背景</p><div class="theme-grid">';
        THEMES.forEach(function (t) {
            const active = t.id === current ? ' is-active' : '';
            html += '<button type="button" class="theme-option' + active + '" data-theme-option="' + t.id + '" aria-pressed="' + (active ? 'true' : 'false') + '">';
            html += '<span class="theme-swatch" style="background:linear-gradient(135deg,' + t.swatch[0] + ' 50%,' + t.swatch[1] + ' 50%)"></span>';
            html += '<span class="theme-option-label">' + t.label + '</span></button>';
        });
        html += '</div></div></div>';
        root.innerHTML = html;

        const trigger = root.querySelector('[data-theme-trigger]');
        const panel = root.querySelector('[data-theme-panel]');

        function closePanel() {
            if (panel) panel.hidden = true;
            if (trigger) trigger.setAttribute('aria-expanded', 'false');
        }

        function openPanel() {
            if (panel) panel.hidden = false;
            if (trigger) trigger.setAttribute('aria-expanded', 'true');
        }

        if (trigger && panel) {
            trigger.onclick = function (e) {
                e.stopPropagation();
                panel.hidden ? openPanel() : closePanel();
            };
        }

        root.querySelectorAll('[data-theme-option]').forEach(function (btn) {
            btn.onclick = function (e) {
                e.stopPropagation();
                apply(btn.getAttribute('data-theme-option'));
                closePanel();
            };
        });

        document.addEventListener('click', function (e) {
            if (!root.contains(e.target)) closePanel();
        });
    }

    return { THEMES, get, apply, init, mountPicker, STORAGE_KEY, DEFAULT };
})();

(function () {
    try {
        const saved = localStorage.getItem('sc_demo_theme');
        if (saved) {
            document.documentElement.setAttribute('data-theme', saved);
        }
    } catch (e) { /* ignore */ }
})();
