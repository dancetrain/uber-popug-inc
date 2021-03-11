import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

import { Container, Jumbotron } from "react-bootstrap";

import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { QueryClient, QueryClientProvider } from "react-query";
import MainPage from "./pages/MainPage/MainPage";

library.add(fas);

const queryClient = new QueryClient()

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <div className="App">
                <Jumbotron className="text-center">
                    <h1>PopugJira</h1>
                </Jumbotron>
                <hr />
                <Container>
                  <MainPage />
                </Container>
            </div>
        </QueryClientProvider>
    );
}

export default App;
