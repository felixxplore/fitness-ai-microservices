 import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx"; 
import { AuthProvider } from "react-oauth2-code-pkce";
import { authConfig } from "./config/authConfig.js";
import { BrowserRouter } from "react-router";

createRoot(document.getElementById("root")).render(
  <AuthProvider authConfig={authConfig}>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </AuthProvider>,
);
