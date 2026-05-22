/**
 * 管理中心 CRUD：数据持久化在 cloud-business 模块 data/demo/*.json。
 */
const MockCrudRegistry = (function () {
    const API_STORAGE_PATH = '/cloud-business/api/business/storage-path';

    const STATUS_OPTIONS = [
        { value: '1', label: '启用' },
        { value: '0', label: '停用' }
    ];

    const ORDER_STATUS = [
        { value: 'PENDING', label: '待支付' },
        { value: 'PAID', label: '已支付' },
        { value: 'SHIPPED', label: '已发货' },
        { value: 'DONE', label: '已完成' },
        { value: 'CANCEL', label: '已取消' }
    ];

    function labelOf(options, value) {
        const o = options.find((x) => x.value === String(value));
        return o ? o.label : value;
    }

    function statusBadge(value, kind) {
        const label = kind === 'order' ? labelOf(ORDER_STATUS, value) : labelOf(STATUS_OPTIONS, value);
        let cls = 'badge-muted';
        if (kind === 'order') {
            if (value === 'PAID' || value === 'DONE') cls = 'badge-ok';
            else if (value === 'PENDING') cls = 'badge-warn';
            else if (value === 'SHIPPED') cls = 'badge-info';
            else if (value === 'CANCEL') cls = 'badge-danger';
        } else if (value === '1') cls = 'badge-ok';
        return '<span class="badge-pill ' + cls + '">' + escapeHtml(String(label)) + '</span>';
    }

    let toastHost = null;
    function toast(msg, ok) {
        if (!toastHost) {
            toastHost = document.createElement('div');
            toastHost.className = 'toast-host';
            document.body.appendChild(toastHost);
        }
        const el = document.createElement('div');
        el.className = 'toast toast-' + (ok ? 'ok' : 'err');
        el.innerHTML = '<span class="toast-icon">' + (ok ? '✓' : '✕') + '</span><span>' + escapeHtml(msg) + '</span>';
        toastHost.appendChild(el);
        setTimeout(function () { el.remove(); }, 2600);
    }

    const CONFIGS = {
        company: {
            title: '公司管理',
            hint: '数据保存在 cloud-business 目录 data/demo/companies.json',
            apiBase: '/cloud-business/api/companies',
            dataFile: 'companies.json',
            idField: 'id',
            columns: [
                { key: 'id', label: 'ID', width: '60px' },
                { key: 'code', label: '公司编码' },
                { key: 'name', label: '公司名称' },
                { key: 'contact', label: '联系人' },
                { key: 'phone', label: '电话' },
                { key: 'status', label: '状态', renderHtml: (r) => statusBadge(r.status, 'status') },
                { key: 'createTime', label: '创建时间' }
            ],
            fields: [
                { key: 'code', label: '公司编码', required: true },
                { key: 'name', label: '公司名称', required: true },
                { key: 'contact', label: '联系人' },
                { key: 'phone', label: '电话' },
                { key: 'status', label: '状态', type: 'select', options: STATUS_OPTIONS, default: '1' }
            ]
        },
        department: {
            title: '部门管理',
            hint: '数据保存在 data/demo/departments.json',
            apiBase: '/cloud-business/api/departments',
            dataFile: 'departments.json',
            idField: 'id',
            columns: [
                { key: 'id', label: 'ID', width: '60px' },
                { key: 'companyName', label: '所属公司' },
                { key: 'name', label: '部门名称' },
                { key: 'leader', label: '负责人' },
                { key: 'phone', label: '电话' },
                { key: 'status', label: '状态', renderHtml: (r) => statusBadge(r.status, 'status') }
            ],
            fields: [
                { key: 'companyName', label: '所属公司', required: true },
                { key: 'name', label: '部门名称', required: true },
                { key: 'leader', label: '负责人' },
                { key: 'phone', label: '电话' },
                { key: 'status', label: '状态', type: 'select', options: STATUS_OPTIONS, default: '1' }
            ]
        },
        userMgmt: {
            title: '用户管理',
            hint: '数据保存在 data/demo/users.json',
            apiBase: '/cloud-business/api/admin/users',
            dataFile: 'users.json',
            idField: 'id',
            columns: [
                { key: 'id', label: 'ID', width: '60px' },
                { key: 'loginName', label: '登录名' },
                { key: 'userName', label: '姓名' },
                { key: 'deptName', label: '部门' },
                { key: 'role', label: '角色' },
                { key: 'phone', label: '手机' },
                { key: 'status', label: '状态', renderHtml: (r) => statusBadge(r.status, 'status') }
            ],
            fields: [
                { key: 'loginName', label: '登录名', required: true },
                { key: 'userName', label: '姓名', required: true },
                { key: 'deptName', label: '部门' },
                { key: 'role', label: '角色', type: 'select', options: [
                    { value: '超级管理员', label: '超级管理员' },
                    { value: '运营专员', label: '运营专员' },
                    { value: '普通用户', label: '普通用户' }
                ], default: '普通用户' },
                { key: 'phone', label: '手机' },
                { key: 'status', label: '状态', type: 'select', options: STATUS_OPTIONS, default: '1' }
            ]
        },
        productMgmt: {
            title: '产品管理',
            hint: '数据保存在 data/demo/products.json',
            apiBase: '/cloud-business/api/products',
            dataFile: 'products.json',
            idField: 'id',
            columns: [
                { key: 'id', label: 'ID', width: '60px' },
                { key: 'code', label: 'SKU 编码' },
                { key: 'name', label: '产品名称' },
                { key: 'category', label: '分类' },
                { key: 'price', label: '单价(元)' },
                { key: 'stock', label: '库存' },
                { key: 'status', label: '状态', renderHtml: (r) => statusBadge(r.status, 'status') }
            ],
            fields: [
                { key: 'code', label: 'SKU 编码', required: true },
                { key: 'name', label: '产品名称', required: true },
                { key: 'category', label: '分类' },
                { key: 'price', label: '单价', type: 'number', required: true },
                { key: 'stock', label: '库存', type: 'number', default: 100 },
                { key: 'status', label: '状态', type: 'select', options: STATUS_OPTIONS, default: '1' }
            ]
        },
        orderMgmt: {
            title: '订单管理',
            hint: '数据保存在 data/demo/orders.json（与真实下单 API 独立）',
            apiBase: '/cloud-business/api/admin/orders',
            dataFile: 'orders.json',
            idField: 'id',
            enableView: true,
            actionWidth: '200px',
            columns: [
                { key: 'id', label: 'ID', width: '60px' },
                { key: 'orderNo', label: '订单号' },
                { key: 'buyerName', label: '买家' },
                { key: 'productName', label: '商品' },
                { key: 'quantity', label: '数量' },
                { key: 'amount', label: '金额(元)' },
                { key: 'status', label: '状态', renderHtml: (r) => statusBadge(r.status, 'order') },
                { key: 'createTime', label: '下单时间' }
            ],
            fields: [
                { key: 'orderNo', label: '订单号' },
                { key: 'buyerName', label: '买家', required: true },
                { key: 'productCode', label: 'SKU', required: true },
                { key: 'productName', label: '商品名称', required: true },
                { key: 'quantity', label: '数量', type: 'number', default: 1 },
                { key: 'amount', label: '金额', type: 'number', required: true },
                { key: 'status', label: '状态', type: 'select', options: ORDER_STATUS, default: 'PAID' },
                { key: 'createTime', label: '下单时间' }
            ]
        }
    };

    class ApiStore {
        constructor(config) {
            this.config = config;
            this.apiBase = config.apiBase;
            this.rows = [];
            this.storagePath = '';
        }

        async fetchStoragePath() {
            try {
                const res = await apiRequest('GET', API_STORAGE_PATH, null, true);
                if (res.code === 200 && res.data) {
                    this.storagePath = res.data;
                }
            } catch (e) { /* ignore */ }
        }

        async load() {
            const res = await apiRequest('GET', this.apiBase, null, true);
            if (res.code !== 200) {
                throw new Error(res.message || '加载失败');
            }
            this.rows = res.data || [];
            return this.rows;
        }

        list() {
            return this.rows.slice();
        }

        get(id) {
            return this.rows.find((r) => String(r.id) === String(id));
        }

        async create(row) {
            const res = await apiRequest('POST', this.apiBase, row, true);
            if (res.code !== 200) {
                throw new Error(res.message || '新增失败');
            }
            await this.load();
            return res.data;
        }

        async update(id, row) {
            const res = await apiRequest('PUT', this.apiBase + '/' + id, row, true);
            if (res.code !== 200) {
                throw new Error(res.message || '更新失败');
            }
            await this.load();
            return res.data;
        }

        async remove(id) {
            const res = await apiRequest('DELETE', this.apiBase + '/' + id, null, true);
            if (res.code !== 200) {
                throw new Error(res.message || '删除失败');
            }
            await this.load();
            return true;
        }

        async reset() {
            const res = await apiRequest('POST', this.apiBase + '/reset', null, true);
            if (res.code !== 200) {
                throw new Error(res.message || '重置失败');
            }
            this.rows = res.data || [];
            return this.rows;
        }
    }

    class MockCrudPanel {
        constructor(key, config) {
            this.key = key;
            this.config = config;
            this.store = new ApiStore(config);
            this.editingId = null;
            this.keyword = '';
            this.loading = false;
        }

        async render(root) {
            this.root = root;
            root.innerHTML = '<div class="crud-panel"><div class="crud-skeleton"></div><div class="crud-loading"><div class="crud-loading-spinner"></div>加载中…</div></div>';
            root.innerHTML = '<div class="crud-panel"><div class="crud-skeleton"></div><div class="crud-loading"><div class="crud-loading-spinner"></div>加载中…</div></div>';
            root.innerHTML = '<div class="crud-panel"><div class="crud-skeleton"></div><div class="crud-loading"><div class="crud-loading-spinner"></div>加载中…</div></div>';
            root.innerHTML = '<div class="crud-panel"><div class="crud-skeleton"></div><div class="crud-loading"><div class="crud-loading-spinner"></div>加载中…</div></div>';
            try {
                await this.store.fetchStoragePath();
                await this.store.load();
            } catch (e) {
                root.innerHTML = '<p class="hint" style="color:#f87171">' + escapeHtml(e.message) + '</p>';
                return;
            }
            root.innerHTML = '';
            const panel = document.createElement('div');
            panel.className = 'crud-panel';
            panel.appendChild(this.buildToolbar());
            panel.appendChild(this.buildTableWrap());
            panel.appendChild(this.buildModal());
            root.appendChild(panel);
            this.refreshTable();
        }

        buildToolbar() {
            const bar = document.createElement('div');
            bar.className = 'crud-toolbar';
            const pathHint = this.store.storagePath
                ? '存储路径：' + this.store.storagePath + '/' + this.config.dataFile
                : this.config.hint;
            bar.innerHTML =
                '<div class="crud-toolbar-left">' +
                '<h2>' + this.config.title + '</h2>' +
                '<p class="hint">' + escapeHtml(pathHint) + '</p>' +
                '<span class="crud-count" data-total></span>' +
                '</div>' +
                '<div class="crud-toolbar-right">' +
                '<div class="search-wrap"><span class="search-icon" aria-hidden="true">⌕</span>' +
                '<input type="search" class="crud-search" placeholder="搜索关键字…" autocomplete="off" />' +
                '</div>' +
                '<button type="button" class="btn btn-ghost btn-sm" data-act="refresh">↻ 刷新</button>' +
                '<button type="button" class="btn btn-sm" data-act="add">＋ 新增</button>' +
                '<button type="button" class="btn btn-ghost btn-sm" data-act="reset">重置数据</button>' +
                '</div>';
            const search = bar.querySelector('.crud-search');
            search.addEventListener('input', (e) => {
                this.keyword = e.target.value.trim().toLowerCase();
                this.refreshTable();
            });
            this.countEl = bar.querySelector('[data-total]');
            bar.querySelector('[data-act="add"]').onclick = () => this.openModal();
            bar.querySelector('[data-act="refresh"]').onclick = async () => {
                const btn = bar.querySelector('[data-act="refresh"]');
                btn.disabled = true;
                try {
                    await this.store.load();
                    this.refreshTable();
                    toast('数据已刷新', true);
                } catch (e) {
                    toast(e.message || '刷新失败', false);
                } finally {
                    btn.disabled = false;
                }
            };
            bar.querySelector('[data-act="reset"]').onclick = async () => {
                if (!confirm('确定恢复为初始种子数据？')) return;
                try {
                    await this.store.reset();
                    this.refreshTable();
                } catch (e) {
                    toast(e.message || '重置失败');
                }
            };
            return bar;
        }

        buildTableWrap() {
            const wrap = document.createElement('div');
            wrap.className = 'table-wrap card';
            const scroll = document.createElement('div');
            scroll.className = 'table-scroll';
            scroll.innerHTML = '<table class="data-table"><thead></thead><tbody></tbody></table>';
            wrap.appendChild(scroll);
            this.tableHead = scroll.querySelector('thead');
            this.tableBody = scroll.querySelector('tbody');
            return wrap;
        }

        buildModal() {
            const overlay = document.createElement('div');
            overlay.className = 'modal-overlay';
            overlay.hidden = true;
            const formId = 'crud-form-' + this.key;
            overlay.innerHTML =
                '<div class="modal">' +
                '<div class="modal-header"><h3 class="modal-title">编辑</h3>' +
                '<button type="button" class="modal-close" aria-label="关闭">&times;</button></div>' +
                '<form id="' + formId + '" class="modal-form"></form>' +
                '<div class="modal-footer">' +
                '<button type="button" class="btn btn-ghost" data-act="cancel">取消</button>' +
                '<button type="submit" class="btn" data-act="save" form="' + formId + '">保存</button>' +
                '</div></div>';
            overlay.querySelector('.modal-close').onclick = () => this.closeModal();
            overlay.querySelector('[data-act="cancel"]').onclick = () => this.closeModal();
            this.formEl = overlay.querySelector('form');
            this.formEl.onsubmit = (e) => {
                e.preventDefault();
                this.saveForm();
            };
            overlay.querySelector('[data-act="save"]').onclick = (e) => {
                e.preventDefault();
                if (this.formEl.reportValidity()) {
                    this.saveForm();
                }
            };
            overlay.addEventListener('click', (e) => {
                if (e.target === overlay) this.closeModal();
            });
            this.modal = overlay;
            this.modalTitle = overlay.querySelector('.modal-title');
            this.saveBtn = overlay.querySelector('[data-act="save"]');
            return overlay;
        }

        filteredRows() {
            const rows = this.store.list();
            if (!this.keyword) return rows;
            const kw = this.keyword;
            return rows.filter((r) => {
                return Object.keys(r).some((k) => String(r[k]).toLowerCase().indexOf(kw) >= 0);
            });
        }

        refreshTable() {
            const cols = this.config.columns;
            let headHtml = '<tr>';
            cols.forEach((c) => {
                headHtml += '<th' + (c.width ? ' style="width:' + c.width + '"' : '') + '>' + c.label + '</th>';
            });
            const actionWidth = this.config.actionWidth || '140px';
            headHtml += '<th style="width:' + actionWidth + '">操作</th></tr>';
            this.tableHead.innerHTML = headHtml;

            const all = this.store.list();
            const rows = this.filteredRows();
            if (this.countEl) this.countEl.textContent = '共 ' + all.length + ' 条';
            if (!rows.length) {
                const msg = this.keyword ? '没有匹配的记录' : '暂无数据，点击「新增」创建记录';
                this.tableBody.innerHTML = '<tr><td colspan="' + (cols.length + 1) + '" class="empty-cell"><span class="empty-state-icon">📭</span>' + escapeHtml(msg) + '</td></tr>';
                return;
            }
            let bodyHtml = '';
            const self = this;
            rows.forEach((r) => {
                bodyHtml += '<tr>';
                cols.forEach((c) => {
                    let cell;
                    if (c.renderHtml) cell = c.renderHtml(r);
                    else if (c.render) cell = escapeHtml(String(c.render(r)));
                    else cell = escapeHtml(r[c.key] != null ? String(r[c.key]) : '—');
                    bodyHtml += '<td>' + cell + '</td>';
                });
                bodyHtml += '<td class="actions">';
                if (self.config.enableView) {
                    bodyHtml += '<button type="button" class="btn-link" data-view="' + r.id + '">查看</button>';
                }
                bodyHtml += '<button type="button" class="btn-link" data-edit="' + r.id + '">编辑</button>' +
                    '<button type="button" class="btn-link danger" data-del="' + r.id + '">删除</button>' +
                    '</td></tr>';
            });
            this.tableBody.innerHTML = bodyHtml;
            if (self.config.enableView) {
                this.tableBody.querySelectorAll('[data-view]').forEach((btn) => {
                    btn.onclick = () => openOrderDetail(btn.getAttribute('data-view'));
                });
            }
            this.tableBody.querySelectorAll('[data-edit]').forEach((btn) => {
                btn.onclick = () => self.openModal(btn.getAttribute('data-edit'));
            });
            this.tableBody.querySelectorAll('[data-del]').forEach((btn) => {
                btn.onclick = async () => {
                    const id = btn.getAttribute('data-del');
                    if (!confirm('确定删除该记录？')) return;
                    try {
                        await self.store.remove(id);
                        self.refreshTable();
                        toast('已删除', true);
                    } catch (e) {
                        toast(e.message || '删除失败', false);
                    }
                };
            });
        }

        openModal(id) {
            this.editingId = id || null;
            const isEdit = !!id;
            const row = isEdit ? this.store.get(id) : {};
            this.modalTitle.textContent = (isEdit ? '编辑' : '新增') + ' - ' + this.config.title;
            let html = '';
            this.config.fields.forEach((f) => {
                const val = row[f.key] != null ? row[f.key] : (f.default != null ? f.default : '');
                html += '<label>' + f.label + (f.required ? ' *' : '') + '</label>';
                if (f.type === 'select') {
                    html += '<select name="' + f.key + '"' + (f.required ? ' required' : '') + '>';
                    f.options.forEach((o) => {
                        html += '<option value="' + escapeAttr(o.value) + '"' +
                            (String(val) === String(o.value) ? ' selected' : '') + '>' + escapeHtml(o.label) + '</option>';
                    });
                    html += '</select>';
                } else {
                    const type = f.type || 'text';
                    html += '<input name="' + f.key + '" type="' + type + '" value="' + escapeAttr(val) + '"' +
                        (f.required ? ' required' : '') + '/>';
                }
            });
            this.formEl.innerHTML = html;
            this.modal.hidden = false;
            document.body.classList.add('modal-open');
            requestAnimationFrame(() => this.modal.classList.add('is-visible'));
        }

        closeModal() {
            this.modal.classList.remove('is-visible');
            document.body.classList.remove('modal-open');
            this.modal.hidden = true;
            this.editingId = null;
        }

        collectFormData() {
            const data = {};
            this.config.fields.forEach((f) => {
                const el = this.formEl.querySelector('[name="' + f.key + '"]');
                let val = el ? el.value : '';
                if (f.type === 'number' && val !== '') {
                    val = Number(val);
                }
                data[f.key] = val;
            });
            return data;
        }

        async saveForm() {
            if (!this.formEl.reportValidity()) {
                return;
            }
            const data = this.collectFormData();
            const btn = this.saveBtn;
            const oldText = btn ? btn.textContent : '';
            if (btn) {
                btn.disabled = true;
                btn.textContent = '保存中…';
            }
            try {
                if (this.editingId) {
                    await this.store.update(this.editingId, data);
                    toast('保存成功', true);
                } else {
                    await this.store.create(data);
                    toast('新增成功', true);
                }
                this.closeModal();
                this.refreshTable();
            } catch (e) {
                toast(e.message || '保存失败', false);
            } finally {
                if (btn) {
                    btn.disabled = false;
                    btn.textContent = oldText;
                }
            }
        }
    }

    function escapeHtml(s) {
        return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }

    function escapeAttr(s) {
        return escapeHtml(String(s)).replace(/'/g, '&#39;');
    }

    const instances = {};

    function openOrderDetail(orderId) {
        if (typeof window.switchPage === 'function') {
            window.switchPage('vehiclePurchase');
        }
        if (typeof VehiclePurchase !== 'undefined' && VehiclePurchase.showOrderDetail) {
            VehiclePurchase.showOrderDetail(orderId, false);
        }
    }

    return {
        openOrderDetail: openOrderDetail,
        mount(key, rootEl) {
            if (!CONFIGS[key]) return;
            if (!instances[key]) {
                instances[key] = new MockCrudPanel(key, CONFIGS[key]);
            }
            instances[key].render(rootEl);
        },
        refresh(key) {
            if (instances[key] && instances[key].root) {
                instances[key].store.load().then(function () {
                    instances[key].refreshTable();
                }).catch(function (e) {
                    toast(e.message || '刷新失败');
                });
            }
        }
    };
})();
