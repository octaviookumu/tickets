"use client";

import { useEffect } from "react";
import { useAuth } from "react-oidc-context";

export default function LoginPage() {
  const { isLoading, isAuthenticated, signinRedirect } = useAuth();

  useEffect(() => {
    if (isLoading) {
      return;
    }
    if (!isAuthenticated) {
      signinRedirect();
    }
  }, [isLoading, isAuthenticated, signinRedirect]);

  return <div>Redirecting to login...</div>;
}
