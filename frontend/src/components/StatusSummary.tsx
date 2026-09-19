import type { StatusSummary as Summary } from '../types/ServiceStatus';

export default function StatusSummary({ summary }: { summary: Summary }) {
  const items = [
    { label: 'Total services', value: summary.total, className: 'total' },
    { label: 'Operational', value: summary.operational, className: 'operational' },
    { label: 'Degraded', value: summary.degraded, className: 'degraded' },
    { label: 'Down', value: summary.down, className: 'down' },
  ];
  return <section aria-labelledby="summary-heading">
    <h2 id="summary-heading" className="sr-only">All services summary</h2>
    <dl className="summary-grid">{items.map(item =>
      <div className={`summary-card ${item.className}`} key={item.label}>
        <dt><span className="status-dot" aria-hidden="true" />{item.label}</dt>
        <dd>{item.value}</dd>
      </div>,
    )}</dl>
  </section>;
}
