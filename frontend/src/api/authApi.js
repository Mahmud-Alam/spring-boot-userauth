import api from "./axiosInstance";
import refreshApi from "./refreshAxiosInstance";

export const loginUser = async (data) => {
  return api.post("/auth/login", data);
};

export const registerUser = async (data) => {
  return api.post("/auth/register", data);
};

export const refreshAccessToken = async (data) => {
  return refreshApi.post("/auth/refresh-token", data);
};
