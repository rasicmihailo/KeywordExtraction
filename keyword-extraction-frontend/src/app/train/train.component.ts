import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TrainTest } from '../shared/train-test.model';
import { Keyword } from '../shared/keyword.model';

@Component({
  selector: 'app-train',
  templateUrl: './train.component.html',
  styleUrls: ['./train.component.css']
})
export class TrainComponent implements OnInit {
  text: string = '';
  keywords: Keyword[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit() {
  }
  btnClicked() {
    this.http.post(`http://localhost:8080/train`, new TrainTest('\nstop11stop11stop\n' + this.text)).subscribe(
      data => {
        this.keywords = JSON.parse(JSON.stringify(data));
      },
      error => {
      }
  );;
  }
}
