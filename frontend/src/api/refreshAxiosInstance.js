import axios from "axios";

const API_BASE = import.meta.env.VITE_API_BASE;

const refreshApi = axios.create({
  baseURL: `${API_BASE}/api`,
});

export default refreshApi;
