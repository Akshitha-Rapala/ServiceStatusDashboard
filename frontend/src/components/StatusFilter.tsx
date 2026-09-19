import { statusLabels, type StatusFilterValue } from '../types/ServiceStatus';

const filters: StatusFilterValue[] = ['ALL', 'OPERATIONAL', 'DEGRADED', 'DOWN'];
export default function StatusFilter({ value, onChange }: {
  value: StatusFilterValue; onChange: (value: StatusFilterValue) => void;
}) {
  return <div className="filters" role="group" aria-label="Filter services by status">
    {filters.map(filter => <button key={filter} type="button"
      aria-pressed={value === filter} onClick={() => onChange(filter)}>
      {filter === 'ALL' ? 'All' : statusLabels[filter]}
    </button>)}
  </div>;
}
