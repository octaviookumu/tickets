"use client";

import { AuthProvider } from "react-oidc-context";

const keycloakUrl = process.env.NEXT_PUBLIC_KEYCLOAK_URL || "http://localhost:9090";
const keycloakRealm = process.env.NEXT_PUBLIC_KEYCLOAK_REALM || "event-ticket-platform";
const keycloakClientId = process.env.NEXT_PUBLIC_KEYCLOAK_CLIENT_ID || "event-ticket-api";

const oidcConfig = {
  authority: `${keycloakUrl}/realms/${keycloakRealm}`,
  client_id: keycloakClientId,
  redirect_uri:
    typeof window !== "undefined"
      ? `${window.location.origin}/callback`
      : "http://localhost:3000/callback",
};

export function Providers({ children }: { children: React.ReactNode }) {
  return <AuthProvider {...oidcConfig}>{children}</AuthProvider>;
}
