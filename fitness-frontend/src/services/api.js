import axios from "axios";

const API_URL = "http://localhost:8085/api";

const api = axios.create({
  baseURL: API_URL,
});
 
export const getActivities = (token) =>
  axios.get(`${API_URL}/activities`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

export const addActivity = (activity, token) =>
  axios.post(`${API_URL}/activities`, activity, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

export const getActivityDetail = (id, token) =>
  axios.get(`${API_URL}/recommendations/activity/${id}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });