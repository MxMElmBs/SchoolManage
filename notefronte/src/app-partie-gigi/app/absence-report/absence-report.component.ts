// absence-report.component.ts  
import { Component, OnInit } from '@angular/core';  
import { AttendanceService } from '../services/attendance.service';  

@Component({  
  selector: 'app-absence-report',  
  templateUrl: './absence-report.component.html',  
  styleUrls: ['./absence-report.component.css'],  
})  
export class AbsenceReportComponent implements OnInit {  
  absentees: any[] = [];  

  constructor(private attendanceService: AttendanceService) {}  

  ngOnInit(): void {  
    this.absentees = this.attendanceService.getAbsentees();  
  }  
}