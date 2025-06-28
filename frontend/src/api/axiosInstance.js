import axios from "axios";
import { refreshAccessToken } from "./authApi";

const API_BASE = import.meta.env.VITE_API_BASE;

const api = axios.create({
  baseURL: `${API_BASE}/api`,
});

// Attach access token to every request
api.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem("accessToken");
  if (accessToken) {
    config.headers["Authorization"] = `Bearer ${accessToken}`;
  }
  return config;
});

// Handle response errors (like token expiry)
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // If access token expired (401), request wasn't retried, and it's not refresh-token endpoint itself
    if (
      error.response?.status === 401 &&
      !originalRequest._retry &&
      !originalRequest.url.includes("/auth/refresh-token")
    ) {
      console.log(error);

      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem("refreshToken");
        console.log(refreshToken + "______________");

        if (!refreshToken) {
          throw new Error("No refresh token found.");
        }

        // Call backend to get new access token
        const res = await refreshAccessToken({ refreshToken });
        const newAccessToken = res.data.accessToken;

        // Save new access token
        localStorage.setItem("accessToken", newAccessToken);

        // Update original request's header
        originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;

        // Retry the original request
        return api(originalRequest);
      } catch (refreshError) {
        // If refresh token expired/invalid — logout user
        console.log(refreshToken + "NNNNNNNNNNNNNNNNN");
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.location.href = "/login";
      }
    }

    // If error wasn't 401 or something else failed
    return Promise.reject(error);
  }
);

export default api;
