import React from 'react';
import { Col, Container, Row } from 'react-bootstrap';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import './App.css';
import NavigationBar from './components/NavigationBar';
import Login from './views/Login';
import NotFound from './views/NotFound';
import Posts from './views/Posts';
import Welcome from './views/Welcome';

class App extends React.Component {
  render() {
    return (
      <Router>
        <NavigationBar />
        <Container>
          <Row>
            <Col lg={12} className={"margin-top"}>
              <Routes>
                <Route path="/" element={ <Welcome /> } />
                <Route path="/login" element={ <Login /> } />
                <Route path="/posts" element={ <Posts /> } />
                <Route path="*" element={ <NotFound /> } />
              </Routes>
            </Col>
          </Row>
        </Container>
      </Router>
    );
  }
}

export default App;
