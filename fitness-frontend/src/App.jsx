import {
  BrowserRouter as Router,
  Route,
  Routes,
  Navigate,
  useNavigate,
} from "react-router";
import "./App.css";
import { Box, Button } from "@mui/material";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import { logout, setCredentials } from "./store/slices/authSlice";
import ActivityForm from "./components/ActivityForm";
import ActivityList from "./components/ActivityList";
import ActivityDetail from "./components/ActivityDetail";

const ActivitiesPage = () => {
  return (
    <Box sx={{ p: 2, border: "1px dashed grey" }}>
        <ActivityForm onActivityAdded = { () => window.location.reload()}/>
      <ActivityList />
    </Box>
  );
};

function App() {
  const { token, tokenData, logIn, logOut, isAuthenticated } =
    useContext(AuthContext);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch]);

  const handleLogout = () => {
    dispatch(logout());
     
  };
  return (
    <>
     <div>
        {!token ? (
          <Button
            variant="contained"
            onClick={() => {
              logIn();
            }}
          >
            Login
          </Button>
        ) : (
          <Box component="section" sx={{ p: 2, border: "1px dashed grey" }}>
            <Button variant="contained" onClick={handleLogout}>
              Logout
            </Button>

            <Routes>
              <Route path="/activities" element={<ActivitiesPage />} />
              <Route path="/activities/:id" element={<ActivityDetail />} />
              <Route
                path="/"
                element={
                  token ? (
                    <Navigate to="activities" replace />
                  ) : (
                    <div>Welcome! Please</div>
                  )
                }
              />
            </Routes>
          </Box>
        )}
      </div>
    </>
  );
}

export default App;
