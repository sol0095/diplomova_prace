import React from 'react';
import './App.css';
import Loader from 'react-loader-spinner';

import axios from 'axios';

export default class App extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      query: "",
      grammar: '0',
      data: []
    }
  }


  getQueries(){
    this.setState({data: [], loadingData: true});

    axios.get(`/query_data`, {params: {query: this.state.query, grammar: this.state.grammar}})
    .then(res => {
      console.log(res);
      if(res.data != null && res.data.length > 0){
        this.setState({data: res.data}, ()=>console.log(this.state.data));
      }
      else{
        console.log("error in gramamr");
      }

    }).catch(function (error) {
      console.log(error);
   }).finally(response =>{
     this.setState({loadingData: false});
   });
  }


  findOnWeb(rowId){
    if(rowId > -1){
      window.open('https://www.stackoverflow.com/questions/' + rowId, "_blank")
    }
    else{
      alert("Cannot redirect to stackowerflow because of incorrect or empty query!");
    }
  }


  render() {

    const listItems = this.state.data.map((d) =>
      <tr className="item" onClick={()=>this.findOnWeb(d.rowId)}>
        <td className="item-similarity">{d.similarity + " %"}</td>
        <td className="item-query">{d.query}</td>
      </tr>
    );
   
  
    return (
      <React.Fragment>
        <div className="header">
          <h1>Query Comparator</h1>
          <p>Search for most similar queries.</p>
        </div>
        
        <div class="row">
          <div className="column side">
            <h2>Set Params</h2>
            <textarea placeholder="Type your query here..." value={this.state.query}  onChange={(event)=>this.setState({query: event.target.value})}></textarea>
          
            <div className="radio-toolbar" onChange={(event)=>this.setState({grammar: event.target.value})}>
              <input id="radio-mysql" type="radio" value="0" name="grammar" checked={this.state.grammar === "0"}/> 
              <label for="radio-mysql">MySQL</label>
              <input id="radio-sqlite" type="radio" value="1" name="grammar" checked={this.state.grammar === "1"}/> 
              <label for="radio-sqlite">SQLite</label>
              <input id="radio-pgsql" type="radio" value="2" name="grammar" checked={this.state.grammar === "2"}/> 
              <label for="radio-pgsql">PgSQL</label>
              <input id="radio-tsql" type="radio" value="3" name="grammar" checked={this.state.grammar === "3"}/> 
              <label for="radio-tsql">T-SQL</label>
            </div>

            <button className="submit-button" type="submit"  onClick={()=>this.getQueries()}>Search!</button>
          </div>

          <div className="column middle">
            <h2>Top 20</h2>

            {
              this.state.loadingData && <Loader type="Oval" color="#4c4" height={200} width={80}/>
            }

            {
              !this.state.loadingData && 
              <table className="items">
              <thead>
                <tr>
                  <th className="item-similarity-title">Similarity</th>
                  <th className="item-query-title">Query</th>
                </tr>
              </thead>
              <tbody> 
              {
                this.state.data != null &&  listItems
              }
              </tbody>
            </table>
            }
            
          </div>

          <div className="column side">
            <h2>History</h2>
          </div>

        </div>

        <div className="bottom">

        </div>
        
      </React.Fragment>
    )
  }

}



/*
function ble(){
  const listItems = this.state.data.map((d) => <div>{d.similarity}{d.query}</div>);
  const a =
  <div>
  <input value={this.state.query}  onChange={(event)=>this.setState({query: event.target.value})}></input>
  <button onClick={()=>this.getQueries()}></button>
  <div>
    {listItems}
  </div>
</div>
}*/