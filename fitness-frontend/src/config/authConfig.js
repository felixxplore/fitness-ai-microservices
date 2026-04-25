export const authConfig = {
  clientId: "fitness-frontend",
  authorizationEndpoint:
    "http://localhost:8080/realms/fitness-realm/protocol/openid-connect/auth",
  tokenEndpoint:
    "http://localhost:8080/realms/fitness-realm/protocol/openid-connect/token",
  logoutEndpoint:
    "http://localhost:8080/realms/fitness-realm/protocol/openid-connect/logout",
  redirectUri: "http://localhost:5173",
  scope: "openid email offline_access profile",
  autoLogin: false,
  onRefreshTokenExpire: (auth) => auth.logIn(),
};
