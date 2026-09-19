import { useEffect, useState } from 'react';
import StatusSummary from './components/StatusSummary';
import ServiceCard from './components/ServiceCard';
import StatusFilter from './components/StatusFilter';
import { fetchStatuses, StatusApiError } from './services/statusApi';
import type { DashboardResponse, StatusFilterValue } from './types/ServiceStatus';
import './App.css';

export default function App() {
  const [filter, setFilter] = useState<StatusFilterValue>('ALL');
  const [refresh, setRefresh] = useState(0);
  const [data, setData] = useState<DashboardResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<StatusApiError | null>(null);

  useEffect(() => {
    const controller = new AbortController();
    setLoading(true);
    setError(null);
    fetchStatuses(filter, controller.signal)
      .then(result => { if (!controller.signal.aborted) setData(result); })
      .catch((failure: unknown) => {
        if (!controller.signal.aborted) setError(failure instanceof StatusApiError
          ? failure : new StatusApiError('We could not load service statuses. Please try again.'));
      })
      .finally(() => { if (!controller.signal.aborted) setLoading(false); });
    return () => controller.abort();
  }, [filter, refresh]);

  const condition = !data ? 'Awaiting service status'
    : data.summary.down > 0 ? 'Some services are unavailable'
    : data.summary.degraded > 0 ? 'Some services are degraded' : 'All systems operational';
  const tone = !data ? 'neutral' : data.summary.down > 0 ? 'down'
    : data.summary.degraded > 0 ? 'degraded' : 'operational';
  const retry = () => setRefresh(value => value + 1);

  return <>
    <a className="skip-link" href="#main">Skip to dashboard</a>
    <div className="topbar"><div className="topbar-inner"><span className="brand-icon" aria-hidden="true">P</span><span>Platform / <strong>Status</strong></span><span className="environment">INTERNAL</span></div></div>
    <main id="main" className="container">
      <header className="dashboard-header">
        <div><p className="eyebrow">PLATFORM OPERATIONS</p><h1>Service Status</h1><p className="subtitle">Internal Platform Health Dashboard</p></div>
        <button className="refresh-button" onClick={retry} disabled={loading}>
          <span aria-hidden="true">↻</span> {loading ? 'Refreshing…' : 'Refresh'}
        </button>
      </header>
      <section className={`condition-panel ${tone}`} aria-labelledby="condition-heading">
        <div><p className="eyebrow">OVERALL SYSTEM CONDITION</p><h2 id="condition-heading">{condition}</h2>
          <p>{error && data ? 'Showing the last successful snapshot. Current status could not be verified.'
            : data ? `${data.summary.operational} of ${data.summary.total} services operational. Summary includes all services.`
            : 'Connecting to the platform status service.'}</p></div>
        <div className="updated"><span>Last Updated</span>{data
          ? <time dateTime={data.generatedAt}>{new Date(data.generatedAt).toLocaleString(undefined, { dateStyle: 'medium', timeStyle: 'medium' })}</time>
          : <span>Not yet available</span>}</div>
      </section>
      {data && <StatusSummary summary={data.summary} />}
      <section className="services-section" aria-labelledby="services-heading">
        <div className="section-header"><div><h2 id="services-heading">Services</h2><p>Current health of core platform services</p></div>
          <StatusFilter value={filter} onChange={setFilter} /></div>
        <p className="sr-only" role="status" aria-live="polite">{loading ? 'Loading service statuses.' : error ? '' : `${data?.services.length ?? 0} services shown. ${condition}.`}</p>
        {error && <div className="error-panel" role="alert"><h3>Unable to update statuses</h3><p>{error.message}</p>
          {error.requestId && <p className="request-id">Request ID: {error.requestId}</p>}
          <button className="retry-button" onClick={retry}>Try Again</button></div>}
        <div aria-busy={loading}>
          {loading ? <div className="loading-panel"><span className="spinner" aria-hidden="true" /><p>Loading service statuses…</p></div>
            : !error && data && (data.services.length ? <ul className="service-list">{data.services.map(service => <ServiceCard key={service.id} service={service} />)}</ul>
              : <div className="empty-panel">No services match this status.</div>)}
        </div>
      </section>
      <footer><span>Service Status Dashboard</span><span>Mock data · Refresh manually for a new snapshot</span></footer>
    </main>
  </>;
}
