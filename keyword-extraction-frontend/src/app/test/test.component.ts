import { Component, OnInit } from '@angular/core';
import { Keyword } from '../shared/keyword.model';
import { HttpClient } from '@angular/common/http';
import { TrainTest } from '../shared/train-test.model';
import { Advertisement } from '../shared/Advertisement.model';

@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrls: ['./test.component.css']
})
export class TestComponent implements OnInit {

  text: string = '';
  keywords: Keyword[] = [];
  advertisements: Advertisement[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit() {
  }
  btnClicked() {
    this.http.post(`http://localhost:8080/check`, new TrainTest(this.text)).subscribe(
      data => {
        this.keywords = JSON.parse(JSON.stringify(data));
        console.log(this.keywords)
      },
      error => {
      });

  this.http.post(`http://localhost:8080/check-ads`, new TrainTest(this.text)).subscribe(
    data => {
      this.advertisements = JSON.parse(JSON.stringify(data));
      console.log(this.advertisements)
    },
    error => {
    });
  }
}
