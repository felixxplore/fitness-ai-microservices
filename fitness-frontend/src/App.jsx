import { Route, Routes, Navigate } from "react-router";
import "./App.css";
import { Button } from "@mui/material";
import { useContext } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import ActivityDetail from "./components/ActivityDetail";
import ProtectedRoute from "./protectedRoutes/ProtectedRoute";
import ActivitiesPage from "./components/ActivitiesPage";

function App() {
  const { token, logIn, logOut } = useContext(AuthContext);

  if (!token) {
    return (
      <Button variant="contained" onClick={() => logIn()}>
        Login
      </Button>
    );
  }

  return (
    <>
      <Button variant="contained" onClick={() => logOut()}>
        Logout
      </Button>

      <Routes>
        <Route
          path="/activities"
          element={
            <ProtectedRoute>
              <ActivitiesPage />
            </ProtectedRoute>
          }
        />

        <Route
          path="/activities/:id"
          element={
            <ProtectedRoute>
              <ActivityDetail />
            </ProtectedRoute>
          }
        />

        <Route path="/" element={<Navigate to="/activities" replace />} />
      </Routes>
    </>
  );
}

export default App;
