/**
 * 车辆购买：列表 → 详情 → 填写下单 → 订单详情
 */
const VehiclePurchase = (function () {
    const VEHICLE_API = '/cloud-business/api/vehicles';
    const ORDER_API = '/cloud-business/api/admin/orders';

    const ORDER_STATUS_LABEL = {
        PENDING: '待支付',
        PAID: '已支付',
        SHIPPED: '已发货',
        DONE: '已完成',
        CANCEL: '已取消'
    };

    let rootEl = null;
    let state = {
        view: 'list',
        keyword: '',
        vehicleId: null,
        vehicle: null,
        orderId: null,
        submitting: false
    };

    function escapeHtml(s) {
        return String(s).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    }

    function formatMoney(v) {
        if (v == null || v === '') return '-';
        const n = Number(v);
        if (Number.isNaN(n)) return String(v);
        return n.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    }

    function vehicleIcon(brand) {
        const b = (brand || '').slice(0, 1);
        return b || '🚗';
    }

    function setView(view, extra) {
        state.view = view;
        if (extra) Object.assign(state, extra);
        render();
    }

    async function loadVehicles(keyword) {
        const q = keyword ? '?keyword=' + encodeURIComponent(keyword) : '';
        const res = await apiRequest('GET', VEHICLE_API + q, null, true);
        if (res.code !== 200) throw new Error(res.message || '加载车辆失败');
        return res.data || [];
    }

    async function loadVehicle(id) {
        const res = await apiRequest('GET', VEHICLE_API + '/' + id, null, true);
        if (res.code !== 200) throw new Error(res.message || '加载详情失败');
        return res.data;
    }

    async function loadOrder(id) {
        const res = await apiRequest('GET', ORDER_API + '/' + id, null, true);
        if (res.code !== 200) throw new Error(res.message || '加载订单失败');
        return res.data;
    }

    async function loadSystemUser(userId) {
        const res = await apiRequest('GET', '/cloud-system/api/system/users/' + userId, null, true);
        if (res.code !== 200) throw new Error(res.message || '加载用户失败');
        return res.data;
    }

    function renderList() {
        rootEl.innerHTML =
            '<div class="vp-toolbar card">' +
            '<div><h2>车辆购买</h2><p class="hint">浏览在售车辆，支持按品牌/车型/名称查询</p></div>' +
            '<div class="vp-search">' +
            '<input type="search" id="vpKeyword" placeholder="搜索品牌、车型、名称…" value="' + escapeHtml(state.keyword) + '"/>' +
            '<button type="button" class="btn btn-sm" id="vpSearchBtn">查询</button>' +
            '</div></div>' +
            '<div id="vpListHost" class="vp-grid"><div class="vp-loading">加载中…</div></div>';

        const host = rootEl.querySelector('#vpListHost');
        const keywordInput = rootEl.querySelector('#vpKeyword');

        rootEl.querySelector('#vpSearchBtn').onclick = function () {
            state.keyword = keywordInput.value.trim();
            renderListContent(host);
        };
        keywordInput.onkeydown = function (e) {
            if (e.key === 'Enter') {
                state.keyword = keywordInput.value.trim();
                renderListContent(host);
            }
        };

        renderListContent(host);
    }

    async function renderListContent(host) {
        host.innerHTML = '<div class="vp-loading">加载中…</div>';
        try {
            const list = await loadVehicles(state.keyword);
            if (!list.length) {
                host.innerHTML = '<div class="vp-empty card">暂无在售车辆</div>';
                return;
            }
            let html = '';
            list.forEach(function (v) {
                html += '<article class="vp-card card" data-id="' + v.id + '">' +
                    '<div class="vp-card-icon">' + escapeHtml(vehicleIcon(v.brand)) + '</div>' +
                    '<div class="vp-card-body">' +
                    '<h3>' + escapeHtml(v.name) + '</h3>' +
                    '<p class="vp-meta">' + escapeHtml(v.brand) + ' · ' + escapeHtml(v.model) + '</p>' +
                    '<p class="vp-meta">' + escapeHtml(v.color || '-') + ' · ' + escapeHtml(v.fuelType || '-') + '</p>' +
                    '<div class="vp-price">¥ ' + formatMoney(v.price) + '</div>' +
                    '<div class="vp-stock">库存 ' + (v.stock != null ? v.stock : '-') + ' 台</div>' +
                    '</div></article>';
            });
            host.innerHTML = html;
            host.querySelectorAll('.vp-card').forEach(function (card) {
                card.onclick = function () {
                    setView('detail', { vehicleId: Number(card.getAttribute('data-id')) });
                };
            });
        } catch (e) {
            host.innerHTML = '<div class="vp-empty card" style="color:#f87171">' + escapeHtml(e.message) + '</div>';
        }
    }

    async function renderDetail() {
        rootEl.innerHTML = '<div class="vp-loading">加载详情…</div>';
        try {
            const v = await loadVehicle(state.vehicleId);
            state.vehicle = v;
            rootEl.innerHTML =
                '<div class="vp-breadcrumb">' +
                '<button type="button" class="btn-link" id="vpBackList">← 返回列表</button></div>' +
                '<div class="vp-detail card">' +
                '<div class="vp-detail-head">' +
                '<div class="vp-detail-icon">' + escapeHtml(vehicleIcon(v.brand)) + '</div>' +
                '<div><h2>' + escapeHtml(v.name) + '</h2>' +
                '<p class="hint">SKU：' + escapeHtml(v.code) + '</p></div></div>' +
                '<div class="vp-detail-grid">' +
                detailRow('品牌', v.brand) +
                detailRow('车型', v.model) +
                detailRow('颜色', v.color) +
                detailRow('能源', v.fuelType) +
                detailRow('指导价', '¥ ' + formatMoney(v.price)) +
                detailRow('库存', (v.stock != null ? v.stock + ' 台' : '-')) +
                '</div>' +
                '<p class="vp-desc">' + escapeHtml(v.description || '暂无描述') + '</p>' +
                '<div class="vp-actions">' +
                '<button type="button" class="btn" id="vpBuyBtn">立即购买</button>' +
                '</div></div>';

            rootEl.querySelector('#vpBackList').onclick = function () {
                setView('list');
            };
            rootEl.querySelector('#vpBuyBtn').onclick = function () {
                setView('checkout', { vehicle: v });
            };
        } catch (e) {
            rootEl.innerHTML =
                '<div class="card" style="color:#f87171">' + escapeHtml(e.message) +
                '<br><button type="button" class="btn btn-ghost btn-sm" id="vpBackList" style="margin-top:0.75rem">返回列表</button></div>';
            rootEl.querySelector('#vpBackList').onclick = function () { setView('list'); };
        }
    }

    function detailRow(label, value) {
        return '<div class="vp-detail-item"><span class="label">' + escapeHtml(label) +
            '</span><span class="value">' + escapeHtml(value || '-') + '</span></div>';
    }

    async function renderCheckout() {
        const v = state.vehicle;
        if (!v) {
            setView('list');
            return;
        }
        const sessionUser = AppSession.getUser();
        rootEl.innerHTML = '<div class="vp-loading">加载下单信息…</div>';
        let profile = sessionUser;
        try {
            profile = await loadSystemUser(sessionUser.userId);
        } catch (e) { /* 使用会话信息 */ }

        rootEl.innerHTML =
            '<div class="vp-breadcrumb">' +
            '<button type="button" class="btn-link" id="vpBackDetail">← 返回车辆详情</button></div>' +
            '<div class="vp-checkout">' +
            '<div class="card vp-panel"><h3>买家信息</h3>' +
            '<div class="vp-info-grid">' +
            infoRow('用户 ID', profile.userId || sessionUser.userId) +
            infoRow('姓名', profile.userName || sessionUser.userName) +
            infoRow('角色', profile.role || sessionUser.role) +
            '</div></div>' +
            '<div class="card vp-panel"><h3>车辆信息</h3>' +
            '<div class="vp-info-grid">' +
            infoRow('车辆', v.name) +
            infoRow('品牌/车型', (v.brand || '') + ' ' + (v.model || '')) +
            infoRow('颜色', v.color) +
            infoRow('能源', v.fuelType) +
            infoRow('SKU', v.code) +
            infoRow('单价', '¥ ' + formatMoney(v.price)) +
            '</div>' +
            '<label style="margin-top:1rem">购买数量</label>' +
            '<input type="number" id="vpQty" value="1" min="1" max="' + (v.stock || 99) + '"/>' +
            '<label>备注（可选）</label>' +
            '<input type="text" id="vpRemark" placeholder="如：希望周末提车"/>' +
            '</div>' +
            '<div class="card vp-panel vp-submit-panel">' +
            '<p class="hint">提交后将调用真实下单链路（cloud-business → cloud-system），并写入订单管理列表。</p>' +
            '<button type="button" class="btn btn-block" id="vpSubmitBtn">提交订单</button>' +
            '</div></div>';

        rootEl.querySelector('#vpBackDetail').onclick = function () {
            setView('detail', { vehicleId: v.id, vehicle: v });
        };
        rootEl.querySelector('#vpSubmitBtn').onclick = submitOrder;
    }

    function infoRow(label, value) {
        return '<div class="vp-info-row"><span>' + escapeHtml(label) + '</span><strong>' +
            escapeHtml(value != null ? String(value) : '-') + '</strong></div>';
    }

    function orderStatusBadge(status, label) {
        let cls = 'badge-muted';
        if (status === 'PAID' || status === 'DONE') cls = 'badge-ok';
        else if (status === 'PENDING') cls = 'badge-warn';
        else if (status === 'SHIPPED') cls = 'badge-info';
        else if (status === 'CANCEL') cls = 'badge-danger';
        return '<span class="badge-pill ' + cls + '">' + escapeHtml(label) + '</span>';
    }

    function orderField(label, value, highlight) {
        return '<div class="vp-order-field' + (highlight ? ' is-highlight' : '') + '">' +
            '<span class="vp-order-field-label">' + escapeHtml(label) + '</span>' +
            '<span class="vp-order-field-value">' + escapeHtml(value != null ? String(value) : '—') + '</span></div>';
    }

    async function submitOrder() {
        if (state.submitting) return;
        const v = state.vehicle;
        const qtyEl = rootEl.querySelector('#vpQty');
        const remarkEl = rootEl.querySelector('#vpRemark');
        const qty = Number(qtyEl && qtyEl.value ? qtyEl.value : 1);
        if (!qty || qty < 1) {
            alert('购买数量至少为 1');
            return;
        }
        if (v.stock != null && qty > v.stock) {
            alert('超过库存数量');
            return;
        }

        const btn = rootEl.querySelector('#vpSubmitBtn');
        state.submitting = true;
        if (btn) {
            btn.disabled = true;
            btn.textContent = '提交中…';
        }
        try {
            const body = {
                quantity: qty,
                remark: remarkEl ? remarkEl.value.trim() : ''
            };
            const res = await apiRequest('POST', VEHICLE_API + '/' + v.id + '/purchase', body, true);
            if (res.code !== 200 || !res.data) {
                throw new Error(res.message || '下单失败');
            }
            if (typeof MockCrudRegistry !== 'undefined' && MockCrudRegistry.refresh) {
                MockCrudRegistry.refresh('orderMgmt');
            }
            setView('orderDetail', {
                orderId: res.data.adminOrderId,
                vehicle: res.data.vehicle || v,
                fromPurchase: true,
                purchaseResult: res.data
            });
        } catch (e) {
            alert(e.message || '下单失败');
        } finally {
            state.submitting = false;
            if (btn) {
                btn.disabled = false;
                btn.textContent = '提交订单';
            }
        }
    }

    async function tryLoadVehicleByCode(code) {
        if (!code || code.indexOf('CAR-') !== 0) return null;
        try {
            const res = await apiRequest('GET', VEHICLE_API, null, true);
            if (res.code !== 200 || !res.data) return null;
            return res.data.find(function (item) { return item.code === code; }) || null;
        } catch (e) {
            return null;
        }
    }

    async function renderOrderDetail() {
        rootEl.innerHTML = '<div class="vp-loading">加载订单详情…</div>';
        try {
            const order = await loadOrder(state.orderId);
            let v = state.vehicle;
            if (!v && order.productCode) {
                v = await tryLoadVehicleByCode(order.productCode);
            }
            const statusLabel = ORDER_STATUS_LABEL[order.status] || order.status;
            const fromPurchase = !!state.fromPurchase;

            let html = '<div class="vp-order-detail">';
            html += '<div class="vp-breadcrumb">' +
                '<button type="button" class="btn-link" id="vpBackOrderList">← ' +
                (fromPurchase ? '返回' : '返回订单列表') + '</button></div>';

            if (fromPurchase) {
                html += '<div class="vp-success card">' +
                    '<div class="vp-success-icon">✓</div>' +
                    '<h2>下单成功</h2>' +
                    '<p class="hint">订单已创建，可在「订单管理」中查看</p></div>';
            }

            html += '<div class="vp-order-hero card">' +
                '<div class="vp-order-hero-top">' +
                '<div class="vp-order-hero-main">' +
                '<p class="vp-order-hero-label">' + (fromPurchase ? '您的订单' : '订单详情') + '</p>' +
                '<h2 class="vp-order-hero-no">' + escapeHtml(order.orderNo || '—') + '</h2>' +
                '<p class="vp-order-hero-sub">订单 ID · ' + escapeHtml(order.id) + '</p>' +
                '</div>' +
                '<div class="vp-order-hero-status">' + orderStatusBadge(order.status, statusLabel) + '</div>' +
                '</div>' +
                '<div class="vp-order-hero-stats">' +
                '<div class="vp-order-stat is-amount">' +
                '<span class="vp-order-stat-label">订单金额</span>' +
                '<span class="vp-order-stat-value">¥ ' + formatMoney(order.amount) + '</span></div>' +
                '<div class="vp-order-stat">' +
                '<span class="vp-order-stat-label">购买数量</span>' +
                '<span class="vp-order-stat-value">' + escapeHtml(order.quantity) + ' 件</span></div>' +
                '<div class="vp-order-stat">' +
                '<span class="vp-order-stat-label">买家</span>' +
                '<span class="vp-order-stat-value">' + escapeHtml(order.buyerName) + '</span></div>' +
                '<div class="vp-order-stat">' +
                '<span class="vp-order-stat-label">下单时间</span>' +
                '<span class="vp-order-stat-value">' + escapeHtml(order.createTime || '—') + '</span></div>' +
                '</div></div>';

            html += '<div class="vp-order-sections">' +
                '<section class="card vp-order-section">' +
                '<h3 class="vp-section-title"><span class="vp-section-icon" aria-hidden="true">📋</span>订单信息</h3>' +
                '<div class="vp-order-fields">' +
                orderField('商品名称', order.productName, true) +
                orderField('商品 SKU', order.productCode) +
                orderField('订单号', order.orderNo) +
                orderField('订单状态', statusLabel) +
                orderField('买家', order.buyerName) +
                orderField('下单时间', order.createTime) +
                '</div></section>';

            if (v) {
                html += '<section class="card vp-order-section vp-vehicle-section">' +
                    '<h3 class="vp-section-title"><span class="vp-section-icon" aria-hidden="true">🚗</span>关联车辆</h3>' +
                    '<div class="vp-vehicle-summary">' +
                    '<div class="vp-vehicle-summary-icon">' + escapeHtml(vehicleIcon(v.brand)) + '</div>' +
                    '<div class="vp-vehicle-summary-body">' +
                    '<h4>' + escapeHtml(v.name) + '</h4>' +
                    '<p>' + escapeHtml(v.brand) + ' · ' + escapeHtml(v.model) + '</p>' +
                    '<p class="vp-vehicle-summary-meta">' + escapeHtml(v.color || '-') +
                    ' · ' + escapeHtml(v.fuelType || '-') + ' · ¥ ' + formatMoney(v.price) + '</p>' +
                    '</div></div>' +
                    '<div class="vp-order-fields vp-order-fields--compact">' +
                    orderField('车辆编码', v.code) +
                    orderField('颜色', v.color) +
                    orderField('能源类型', v.fuelType) +
                    '</div></section>';
            }

            html += '</div>';

            html += '<div class="vp-order-footer card">';
            if (fromPurchase) {
                html += '<button type="button" class="btn" id="vpGoOrderList">查看订单列表</button>' +
                    '<button type="button" class="btn btn-ghost" id="vpContinueBuy">继续购车</button>';
            } else {
                html += '<button type="button" class="btn" id="vpGoOrderList">返回订单列表</button>';
            }
            html += '</div></div>';

            rootEl.innerHTML = html;

            rootEl.querySelector('#vpGoOrderList').onclick = function () {
                if (typeof window.switchPage === 'function') {
                    window.switchPage('orderMgmt');
                }
            };
            const continueBtn = rootEl.querySelector('#vpContinueBuy');
            if (continueBtn) {
                continueBtn.onclick = function () {
                    setView('list');
                };
            }
            const backList = rootEl.querySelector('#vpBackOrderList');
            if (backList) {
                backList.onclick = function () {
                    if (typeof window.switchPage === 'function') {
                        window.switchPage('orderMgmt');
                    }
                };
            }
        } catch (e) {
            rootEl.innerHTML =
                '<div class="card" style="color:#f87171">' + escapeHtml(e.message) +
                '<br><button type="button" class="btn btn-ghost btn-sm" style="margin-top:0.75rem" id="vpBackList">返回列表</button></div>';
            rootEl.querySelector('#vpBackList').onclick = function () { setView('list'); };
        }
    }

    function render() {
        if (!rootEl) return;
        if (state.view === 'list') renderList();
        else if (state.view === 'detail') renderDetail();
        else if (state.view === 'checkout') renderCheckout();
        else if (state.view === 'orderDetail') renderOrderDetail();
    }

    return {
        mount: function (root) {
            rootEl = root;
            state = { view: 'list', keyword: '', vehicleId: null, vehicle: null, orderId: null, submitting: false };
            render();
        },
        showOrderDetail: function (orderId, fromPurchase) {
            if (!rootEl) {
                rootEl = document.getElementById('vehiclePurchaseRoot');
            }
            if (!rootEl) return;
            setView('orderDetail', {
                orderId: orderId,
                fromPurchase: fromPurchase === true,
                vehicle: null
            });
        }
    };
})();
