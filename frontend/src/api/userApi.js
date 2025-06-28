import api from "./axiosInstance";

export const getProfile = () => {
  return api.get("/users/profile");
};

export const updateProfile = (updateData) => {
  return api.patch("/users/profile", updateData);
};

export const getAllUsers = () => {
  return api.get("/users");
};
