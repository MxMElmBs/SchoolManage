import { Component, AfterViewInit } from '@angular/core';  
import { Chart, registerables, ChartOptions, ChartData } from 'chart.js';  

@Component({  
  selector: 'app-volume-horaire',  
  standalone: true,  
  templateUrl: './volume-horaire.component.html',  
  styleUrls: ['./volume-horaire.component.css']  
})  
export class VolumeHoraireComponent implements AfterViewInit {  

  constructor() {  
    Chart.register(...registerables);  
  }  

  ngAfterViewInit() {  
    this.createChart();  
  }  

  private createChart(): void {  
    const ctx = document.getElementById('myChart') as HTMLCanvasElement;  

    const quotas = {  
      'UE1': { quota: 2, data: [8, 10, 7, 10, 9, 11] },  
      'UE2': { quota: 12, data: [12, 10, 11, 14, 12, 13] },  
      'UE3': { quota: 8, data: [5, 7, 6, 4, 8, 3] },  
    };  

    const labels = ['Sem 1', 'Sem 2', 'Sem 3', 'Sem 4', 'Sem 5', 'Sem 6'];  

    const datasets: {  
      label: string;  
      data: number[];  
      fill: boolean;  
      borderColor: string;  
      tension: number;  
    }[] = [];  

    // Construction des datasets et des couleurs selon le respect du quota  
    Object.entries(quotas).forEach(([ue, { quota, data }]) => {  
      // Vérifie si toutes les heures sont >= au quota  
      const color = data.every(hours => hours >= quota) ? 'rgb(75, 192, 192)' : 'rgb(255, 99, 132)';  
      
      datasets.push({  
        label: ue,  
        data: data,  
        fill: false,  
        borderColor: color, // Couleur verte si respecté, rouge sinon  
        tension: 0.1  
      });  
    });  

    const chartData: ChartData<'line'> = {  
      labels: labels,  
      datasets: datasets,  
    };  

    const chartOptions: ChartOptions<'line'> = {  
      responsive: true,  
      plugins: {  
        legend: {  
          display: true,  
          position: 'top',  
        },  
      },  
      scales: {  
        y: {  
          beginAtZero: true  
        }  
      }  
    };  

    new Chart(ctx, {  
      type: 'line',  
      data: chartData,  
      options: chartOptions  
    });  
  }  
}