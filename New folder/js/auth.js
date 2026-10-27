(function initializeEcoTrackAuth() {
  const tokenKey = "token";
  const sessionKey = "ecotrack.loggedIn";
  const alertKey = "ecotrack.authAlertShown";
  const maximumTimerDelay = 2147483647;
  let expirationTimer = null;
  let scheduledToken = "";

  function isLoginPage() {
    return window.location.pathname.endsWith("/login.html");
  }

  function getLoginUrl() {
    const loginPath = window.location.pathname.includes("/html/")
      ? "login.html"
      : "html/login.html";
    return new URL(loginPath, window.location.href).href;
  }

  function clearSession() {
    localStorage.removeItem(tokenKey);
    sessionStorage.removeItem(sessionKey);
    scheduledToken = "";
    window.clearTimeout(expirationTimer);
  }

  function redirectToLogin() {
    if (!isLoginPage()) {
      window.location.replace(getLoginUrl());
    }
  }

  function invalidateSession(message) {
    clearSession();
    if (!isLoginPage() && sessionStorage.getItem(alertKey) !== "true") {
      sessionStorage.setItem(alertKey, "true");
      window.alert(message);
    }
    redirectToLogin();
  }

  function getExpirationTime(token) {
    const segments = token.split(".");
    if (segments.length !== 3 || segments.some((segment) => !segment)) {
      throw new Error("Formato JWT inválido.");
    }

    const encodedPayload = segments[1].replace(/-/g, "+").replace(/_/g, "/");
    const paddedPayload = encodedPayload.padEnd(Math.ceil(encodedPayload.length / 4) * 4, "=");
    const payloadBytes = Uint8Array.from(atob(paddedPayload), (character) => character.charCodeAt(0));
    const payload = JSON.parse(new TextDecoder().decode(payloadBytes));

    if (payload.exp === undefined) {
      return null;
    }
    if (typeof payload.exp !== "number" || !Number.isFinite(payload.exp)) {
      throw new Error("Expiração JWT inválida.");
    }

    return payload.exp * 1000;
  }

  function scheduleExpirationCheck(token, expirationTime) {
    if (scheduledToken === token) {
      return;
    }

    window.clearTimeout(expirationTimer);
    scheduledToken = token;

    const checkExpiration = () => {
      if (localStorage.getItem(tokenKey) !== token) {
        return;
      }

      const remainingTime = expirationTime - Date.now();
      if (remainingTime <= 0) {
        invalidateSession("Seu tempo de acesso expirou. Faça login novamente.");
        return;
      }

      expirationTimer = window.setTimeout(checkExpiration, Math.min(remainingTime, maximumTimerDelay));
    };

    checkExpiration();
  }

  function getValidToken() {
    const token = localStorage.getItem(tokenKey);
    if (!token) {
      clearSession();
      redirectToLogin();
      return null;
    }

    try {
      const expirationTime = getExpirationTime(token);
      if (expirationTime !== null) {
        if (expirationTime <= Date.now()) {
          invalidateSession("Seu tempo de acesso expirou. Faça login novamente.");
          return null;
        }
        scheduleExpirationCheck(token, expirationTime);
      }
    } catch {
      invalidateSession("Seu token é inválido. Faça login novamente.");
      return null;
    }

    return token;
  }

  function enforcePageSession() {
    if (!isLoginPage()) {
      getValidToken();
    }
  }

  window.EcoTrackAuth = {
    getValidToken,
    handleUnauthorized() {
      invalidateSession("Seu token é inválido ou expirou. Faça login novamente.");
    },
    clearAlertFlag() {
      sessionStorage.removeItem(alertKey);
    }
  };

  enforcePageSession();
  window.addEventListener("pageshow", enforcePageSession);
})();