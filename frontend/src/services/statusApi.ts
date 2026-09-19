import type { ApiError, DashboardResponse, StatusFilterValue } from '../types/ServiceStatus';

const apiUrl = (import.meta.env.VITE_API_URL || 'http://localhost:8080').replace(/\/+$/, '');

export class StatusApiError extends Error {
  constructor(message: string, public readonly requestId?: string) { super(message); }
}

export async function fetchStatuses(filter: StatusFilterValue, signal: AbortSignal): Promise<DashboardResponse> {
  const query = filter === 'ALL' ? '' : `?status=${encodeURIComponent(filter)}`;
  let response: Response;
  try {
    response = await fetch(`${apiUrl}/api/v1/services${query}`, { signal });
  } catch (error) {
    if (signal.aborted) throw error;
    throw new StatusApiError('We could not reach the status service. Check your connection and try again.');
  }
  if (!response.ok) {
    const body = await response.json().catch(() => null) as Partial<ApiError> | null;
    const requestId = body?.requestId || response.headers.get('X-Request-ID') || undefined;
    const message = response.status === 400 && typeof body?.message === 'string'
      ? body.message
      : 'The status service is temporarily unavailable. Please try again.';
    throw new StatusApiError(message, requestId);
  }
  const data = await response.json() as DashboardResponse;
  if (!Array.isArray(data.services) || !data.summary || Number.isNaN(Date.parse(data.generatedAt))) {
    throw new StatusApiError('The status service returned an incomplete response. Please try again.');
  }
  return data;
}
