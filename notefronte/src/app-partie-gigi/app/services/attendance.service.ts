// attendance.service.ts  
import { Injectable } from '@angular/core';  

interface Student {  
  id: number;  
  name: string;  
  present: boolean;  
}  

@Injectable({  
  providedIn: 'root',  
})  
export class AttendanceService {  
  private students: Student[] = [  
    { id: 1, name: 'Alice', present: false },  
    { id: 2, name: 'Bob', present: false },  
    { id: 3, name: 'Charlie', present: false },  
  ];  

  markAttendance(id: number, present: boolean) {  
    const student = this.students.find((s) => s.id === id);  
    if (student) {  
      student.present = present;  
    }  
  }  

  getAbsentees() {  
    return this.students.filter((s) => !s.present);  
  }  

  getStudents() {  
    return this.students;  
  }  
}