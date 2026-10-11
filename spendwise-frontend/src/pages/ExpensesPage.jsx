
import { useEffect, useMemo, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { CalendarDays, Edit2, Plus, Search, Trash2 } from 'lucide-react';
import { expenseApi } from '../api/expenseApi';
import { categoryApi } from '../api/categoryApi';
import ExpenseForm from '../components/ExpenseForm';
import Modal from '../components/Modal';
import Notice from '../components/Notice';
import { dateLabel, errorMessage, money } from '../utils/format';

const getCurrentMonth = () => {
  const now = new Date();
  return `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
};

const getMonthLabel = (value) => {
  if (!value) return '';
  const [year, month] = value.split('-').map(Number);
  return new Date(year, month - 1, 1).toLocaleDateString('en-IN', {
    month: 'long',
    year: 'numeric'
  });
};

export default function ExpensesPage() {
  const [searchParams, setSearchParams] = useSearchParams();

  const [expenses, setExpenses] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [notice, setNotice] = useState(null);
  const [query, setQuery] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('all');
  const [selectedMonth, setSelectedMonth] = useState(getCurrentMonth());
  const [modal, setModal] = useState(null);
  const [saving, setSaving] = useState(false);

  const load = async (monthValue = selectedMonth) => {
    if (!monthValue) return;

    const [year, month] = monthValue.split('-').map(Number);

    setLoading(true);

    try {
      const [expenseData, categoryData] = await Promise.all([
        expenseApi.listByMonth(year, month),
        categoryApi.list()
      ]);

      setExpenses(Array.isArray(expenseData) ? expenseData : []);
      setCategories(Array.isArray(categoryData) ? categoryData : []);
    } catch (err) {
      setNotice({
        type: 'error',
        message: errorMessage(err)
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load(selectedMonth);
  }, [selectedMonth]);

  useEffect(() => {
    if (searchParams.get('new') === '1' && !loading && !modal) {
      setModal({ type: 'create' });
      setSearchParams({}, { replace: true });
    }
  }, [searchParams, loading, modal, setSearchParams]);

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();

    return expenses
      .filter((expense) => {
        const categoryName =
          expense.categoryName ||
          categories.find(
            (category) => String(category.id) === String(expense.categoryId)
          )?.name ||
          '';

        const matchesSearch =
          String(expense.description || '').toLowerCase().includes(q) ||
          categoryName.toLowerCase().includes(q);

        const matchesCategory =
          categoryFilter === 'all' ||
          String(expense.categoryId) === String(categoryFilter);

        return matchesSearch && matchesCategory;
      })
      .sort((a, b) => String(b.date).localeCompare(String(a.date)));
  }, [expenses, categories, query, categoryFilter]);

  const save = async (payload) => {
    setSaving(true);

    try {
      if (modal.type === 'edit') {
        await expenseApi.update(modal.item.id, payload);
      } else {
        await expenseApi.create(payload);
      }

      setModal(null);
      setNotice({
        type: 'success',
        message:
          modal.type === 'edit'
            ? 'Expense updated successfully.'
            : 'Expense added successfully.'
      });

      const expenseMonth = payload.date?.slice(0, 7);

if (expenseMonth && expenseMonth !== selectedMonth) {
  setSelectedMonth(expenseMonth);
} else {
  await load(selectedMonth);
}
    } catch (err) {
      throw errorMessage(err);
    } finally {
      setSaving(false);
    }
  };

  const remove = async (expense) => {
    if (
      !window.confirm(
        `Delete this expense${expense.description ? ` (${expense.description})` : ''}? This cannot be undone.`
      )
    ) {
      return;
    }

    try {
      await expenseApi.remove(expense.id);
      setNotice({ type: 'success', message: 'Expense deleted.' });
      await load(selectedMonth);
    } catch (err) {
      setNotice({ type: 'error', message: errorMessage(err) });
    }
  };

  return (
    <>
      <div className="page-heading">
        <div>
          <span className="eyebrow">TRANSACTIONS</span>
          <h1>Expenses</h1>
          <p className="muted">
            Review and manage your spending by month.
          </p>
        </div>

        <button
          className="button primary"
          onClick={() => setModal({ type: 'create' })}
        >
          <Plus size={17} /> Add expense
        </button>
      </div>

      <Notice notice={notice} onClose={() => setNotice(null)} />

      <section className="panel table-panel">
        <div className="table-toolbar">
          <div className="search-box">
            <Search size={17} />
            <input
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder="Search description or category…"
              aria-label="Search expenses"
            />
          </div>

          <div className="search-box">
            <CalendarDays size={17} />
            <input
              type="month"
              value={selectedMonth}
              max={getCurrentMonth()}
              onChange={(event) => setSelectedMonth(event.target.value)}
              aria-label="Filter expenses by month and year"
            />
          </div>

          <select
            className="filter-select"
            value={categoryFilter}
            onChange={(event) => setCategoryFilter(event.target.value)}
            aria-label="Filter by category"
          >
            <option value="all">All categories</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </div>

        <div className="table-summary">
          <span>
            {loading
              ? 'Loading transactions…'
              : `${filtered.length} ${filtered.length === 1 ? 'transaction' : 'transactions'}`}
          </span>
          <span>{getMonthLabel(selectedMonth)}</span>
        </div>

        {loading ? (
          <div className="empty-state">Loading expenses…</div>
        ) : filtered.length ? (
          <div className="table-scroll">
            <table>
              <thead>
                <tr>
                  <th>Expense</th>
                  <th>Category</th>
                  <th>Date</th>
                  <th className="align-right">Amount</th>
                  <th aria-label="Actions"></th>
                </tr>
              </thead>

              <tbody>
                {filtered.map((expense) => {
                  const categoryName =
                    expense.categoryName ||
                    categories.find(
                      (category) =>
                        String(category.id) === String(expense.categoryId)
                    )?.name ||
                    'Uncategorized';

                  return (
                    <tr key={expense.id}>
                      <td>
                        <div className="table-expense">
                          <span className="transaction-mark">
                            {categoryName.slice(0, 1).toUpperCase()}
                          </span>
                          <span>
                            <strong>{expense.description || 'Expense'}</strong>
                            <small>#{String(expense.id).slice(0, 8)}</small>
                          </span>
                        </div>
                      </td>

                      <td>
                        <span className="category-pill">{categoryName}</span>
                      </td>

                      <td>
                        <span className="date-cell">
                          <CalendarDays size={14} />
                          {dateLabel(expense.date)}
                        </span>
                      </td>

                      <td className="align-right amount-cell">
                        {money(expense.amount)}
                      </td>

                      <td>
                        <div className="row-actions">
                          <button
                            className="icon-button"
                            title="Edit expense"
                            aria-label="Edit expense"
                            onClick={() =>
                              setModal({ type: 'edit', item: expense })
                            }
                          >
                            <Edit2 size={16} />
                          </button>

                          <button
                            className="icon-button danger-icon"
                            title="Delete expense"
                            aria-label="Delete expense"
                            onClick={() => remove(expense)}
                          >
                            <Trash2 size={16} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="empty-state">
            <span className="empty-icon">
              <Search size={24} />
            </span>
            <strong>
              {expenses.length
                ? 'No matching expenses'
                : `No expenses for ${getMonthLabel(selectedMonth)}`}
            </strong>
            <span>
              {expenses.length
                ? 'Try another search or category filter.'
                : 'Try another month or create an expense.'}
            </span>

            {!expenses.length && (
              <button
                className="button primary"
                onClick={() => setModal({ type: 'create' })}
              >
                <Plus size={16} /> Add expense
              </button>
            )}
          </div>
        )}
      </section>

      {modal && (
        <Modal
          title={modal.type === 'edit' ? 'Edit expense' : 'Add an expense'}
          onClose={() => setModal(null)}
        >
          <ExpenseForm
            initial={modal.item}
            categories={categories}
            onSubmit={save}
            onCancel={() => setModal(null)}
            saving={saving}
          />

          {categories.length === 0 && (
            <p className="form-hint">
              You need a category before adding an expense.{' '}
              <Link to="/categories" onClick={() => setModal(null)}>
                Create one here
              </Link>.
            </p>
          )}
        </Modal>
      )}
    </>
  );
}
