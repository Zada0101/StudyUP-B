import React, { createContext, useContext, useState } from 'react'


const AuthCtx = createContext(null) // Context for auth data
export const useAuth = () => useContext(AuthCtx) // Hook to use auth


export function AuthProvider({children}){
const [token, setToken] = useState(() => localStorage.getItem('token')) // Load saved token
const login = (t) => { setToken(t); localStorage.setItem('token', t) } // Save token
const logout = () => { setToken(null); localStorage.removeItem('token') } // Remove token
return <AuthCtx.Provider value={{token, login, logout}}>{children}</AuthCtx.Provider>
}