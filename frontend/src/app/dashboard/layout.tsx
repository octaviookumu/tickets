"use client";

import ProtectedRoute from "@/components/protected-route";
import { Suspense } from "react";

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <Suspense fallback={<p>Loading...</p>}>
      <ProtectedRoute>{children}</ProtectedRoute>
    </Suspense>
  );
}
