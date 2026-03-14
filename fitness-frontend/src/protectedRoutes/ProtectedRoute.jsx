import { useContext } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { Navigate } from "react-router";

const ProtectedRoute = ({ children }) => {
  const { token } = useContext(AuthContext);

  if (!token) {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;
