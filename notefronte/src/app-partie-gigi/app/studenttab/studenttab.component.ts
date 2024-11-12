import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { ChartData, ChartType } from 'chart.js';
import { FormsModule } from '@angular/forms';
import { NgOptimizedImage } from '@angular/common';

@Component({
  selector: 'app-studenttab',
  standalone: true,
  imports: [CommonModule ,FormsModule,MatIconModule,NgOptimizedImage],
  templateUrl: './studenttab.component.html',
  styleUrl: './studenttab.component.css'
})
export class StudenttabComponent {

  
}
