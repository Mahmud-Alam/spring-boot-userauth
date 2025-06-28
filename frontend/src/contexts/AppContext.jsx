import { jwtDecode } from "jwt-decode";
import { createContext, useState, useContext, useEffect } from "react";

export const AppContext = createContext();

export const AppContextProvider = ({ children }) => {
  const [accessToken, setAccessToken] = useState(
    localStorage.getItem("accessToken") || null
  );
  const [refreshToken, setRefreshToken] = useState(
    localStorage.getItem("refreshToken") || null
  );
  const [jwtPayload, setJwtPayload] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      setAccessToken(token);
      const decoded = jwtDecode(token);
      setJwtPayload(decoded);
    }
  }, []);

  const logout = () => {
    setAccessToken(null);
    setRefreshToken(null);
    setJwtPayload(null);
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  };

  const value = {
    accessToken,
    setAccessToken,
    refreshToken,
    setRefreshToken,
    jwtPayload,
    setJwtPayload,
    logout,
  };

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>;
};

export const useAppContext = () => useContext(AppContext);
