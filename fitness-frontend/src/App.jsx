import { BrowserRouter as Router, Route, Routes } from "react-router";
import "./App.css";
import { Button } from "@mui/material";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { useDispatch } from "react-redux";
import { setCredentials } from "./store/slices/authSlice";

function App() {
  const { token, tokenData, logIn, logOut, isAuthenticated } =
    useContext(AuthContext);
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);

  useEffect(() => {
    if (token) {
      dispatch(setCredentials({ token, user: tokenData }));
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch]);
  return (
    <>
      <Router>
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
          <pre>{JSON.stringify(tokenData, null, 2)}</pre>
        )}
      </Router>
    </>
  );
}

export default App;
