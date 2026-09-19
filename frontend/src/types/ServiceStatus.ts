export type ServiceStatus = 'OPERATIONAL' | 'DEGRADED' | 'DOWN';
export type StatusFilterValue = 'ALL' | ServiceStatus;
export interface ServiceInfo {
  id: string;
  name: string;
  description: string;
  status: ServiceStatus;
}
export interface StatusSummary {
  total: number;
  operational: number;
  degraded: number;
  down: number;
}
export interface DashboardResponse {
  services: ServiceInfo[];
  summary: StatusSummary;
  generatedAt: string;
}
export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  requestId: string;
}
export const statusLabels: Record<ServiceStatus, string> = {
  OPERATIONAL: 'Operational', DEGRADED: 'Degraded', DOWN: 'Down',
};
