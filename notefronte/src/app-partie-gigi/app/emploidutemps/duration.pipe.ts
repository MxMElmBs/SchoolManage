import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'duration',
  standalone: true
})
export class DurationPipe implements PipeTransform {

  transform(value: string): string {
    if (!value || !value.startsWith('PT')) {
      return '0 h 0 min';
    }

    const hoursMatch = value.match(/(\d+)H/); 
    const minutesMatch = value.match(/(\d+)M/); 

    const hours = hoursMatch ? parseInt(hoursMatch[1], 10) : 0;
    const minutes = minutesMatch ? parseInt(minutesMatch[1], 10) : 0;

    return `${hours} h ${minutes} min`;
  }

}
