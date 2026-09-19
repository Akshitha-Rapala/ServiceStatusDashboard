import { statusLabels, type ServiceInfo } from '../types/ServiceStatus';

export default function ServiceCard({ service }: { service: ServiceInfo }) {
  return <li className="service-card">
    <div className="service-mark" aria-hidden="true">{service.name === 'AI' ? 'AI' : service.name.slice(0, 2).toUpperCase()}</div>
    <div className="service-details"><h3>{service.name}</h3><p>{service.description}</p></div>
    <span className={`badge ${service.status.toLowerCase()}`}>
      <span className="status-dot" aria-hidden="true" />{statusLabels[service.status]}
    </span>
  </li>;
}
