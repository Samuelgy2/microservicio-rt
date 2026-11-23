const API = '/api/transacciones';
const SIZE = 10;
let page = 0;

const tbody = document.querySelector('#transactionsTable tbody');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const pageNum = document.getElementById('pageNum');
const msg = document.getElementById('msg');
const err = document.getElementById('err');

const money = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP' });

function show(el, text) {
    msg.textContent = '';
    err.textContent = '';
    if (el) el.textContent = text;
}

async function errorMessage(res) {
    try {
        const body = await res.json();
        return body.message || `Error ${res.status}`;
    } catch {
        return `Error ${res.status}`;
    }
}

function cell(row, value) {
    const td = row.insertCell();
    td.textContent = value ?? '';
    return td;
}

async function load(p) {
    try {
        const res = await fetch(`${API}?page=${p}&size=${SIZE}`);
        if (!res.ok) throw new Error(await errorMessage(res));
        const data = await res.json();

        // Si se borró el último registro de la página, retroceder una.
        if (data.content.length === 0 && p > 0) return load(p - 1);

        page = data.page;
        tbody.innerHTML = '';
        for (const t of data.content) {
            const row = tbody.insertRow();
            cell(row, t.id);
            cell(row, t.tipo).className = t.tipo;
            cell(row, money.format(t.monto)).className = 'num';
            cell(row, t.fecha);
            cell(row, t.descripcion);
            cell(row, t.metodoPago);
            cell(row, t.categoriaId);
            cell(row, t.referencia);
            cell(row, t.createdBy);
            cell(row, t.idSocio);
            cell(row, t.estado);
            const btn = document.createElement('button');
            btn.textContent = 'Eliminar';
            btn.className = 'delete';
            btn.addEventListener('click', () => remove(t.id));
            row.insertCell().appendChild(btn);
        }
        if (data.content.length === 0) {
            const td = tbody.insertRow().insertCell();
            td.colSpan = 12;
            td.textContent = 'No hay transacciones';
        }

        pageNum.textContent = `${data.page + 1} de ${Math.max(data.totalPages, 1)} (${data.totalElements} registros)`;
        prevBtn.disabled = data.first;
        nextBtn.disabled = data.last;
    } catch (e) {
        show(err, e.message || 'No se pudo cargar la informacion');
    }
}

async function remove(id) {
    if (!confirm(`¿Eliminar la transaccion ${id}?`)) return;
    try {
        const res = await fetch(`${API}/${id}`, { method: 'DELETE' });
        if (!res.ok) throw new Error(await errorMessage(res));
        const body = await res.json();
        await load(page);
        show(msg, body.message);
    } catch (e) {
        show(err, e.message || 'No se pudo eliminar');
    }
}

prevBtn.addEventListener('click', () => { show(); load(page - 1); });
nextBtn.addEventListener('click', () => { show(); load(page + 1); });

load(0);
