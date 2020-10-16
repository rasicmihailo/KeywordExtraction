import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TrainTest } from '../shared/train-test.model';
import { Keyword } from '../shared/keyword.model';

@Component({
  selector: 'app-train-test',
  templateUrl: './train-test.component.html',
  styleUrls: ['./train-test.component.css']
})
export class TrainTestComponent implements OnInit {
  text: string = '';
  favoriteSeason: string = null;
  seasons: string[] = ['Trening', 'Test'];
  keywords: Keyword[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit() {
  }
  btnClicked() {
    this.http.post(`http://localhost:8080/train`, new TrainTest(this.text)).subscribe(
      data => {
        this.keywords = JSON.parse(JSON.stringify(data));
        console.log(this.keywords)
      },
      error => {
      }
  );;
  }
}
