import { useContext, useEffect, useState } from "react";
import { AuthContext } from "react-oauth2-code-pkce";
import { getActivities } from "../services/api";
import ActivityForm from "./ActivityForm";
import ActivityList from "./ActivityList";
import { Box } from "@mui/material";

const ActivitiesPage = () => {
  const { token } = useContext(AuthContext);
  const [activities, setActivities] = useState([]);

  const fetchActivities = async () => {
    try {
      const response = await getActivities(token);
      setActivities(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (token) {
      fetchActivities();
    }
  }, [token]);

  return (
    <Box sx={{ p: 2, border: "1px dashed grey" }}>
      <ActivityForm onActivityAdded={fetchActivities} />
      <ActivityList activities={activities} />
    </Box>
  );
};

export default ActivitiesPage;
