import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import AppLayout from './layouts/AppLayout';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import DashboardPage from './pages/DashboardPage';
import ExpensesPage from './pages/ExpensesPage';
import CategoriesPage from './pages/CategoriesPage';
function Protected({ children }) { const { isAuthenticated } = useAuth(); return isAuthenticated ? children : <Navigate to="/login" replace/>; }
function AppRoutes() { return <Routes><Route path="/login" element={<LoginPage/>}/><Route path="/register" element={<RegisterPage/>}/><Route element={<Protected><AppLayout/></Protected>}><Route path="/" element={<DashboardPage/>}/><Route path="/expenses" element={<ExpensesPage/>}/><Route path="/categories" element={<CategoriesPage/>}/></Route><Route path="*" element={<Navigate to="/" replace/>}/></Routes>; }
export default function App() { return <BrowserRouter><AuthProvider><AppRoutes/></AuthProvider></BrowserRouter>; }
