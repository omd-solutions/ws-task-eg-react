import React from 'react';
import {AppBar, Button, Toolbar, Typography} from "@material-ui/core";
import {TaskManager} from 'ws-task-react';

const App: React.FC = () => {
    return (
        <div>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6">
                        Task Manager
                    </Typography>
                    <div style={{flexGrow: 1}} />
                    <Button color="inherit"
                            onClick={() => {
                                fetch("/api/start-task").then(() => console.log("Task Started"))
                            }}>
                        Start a Task
                    </Button>
                </Toolbar>
            </AppBar>
            <TaskManager />
        </div>
    );
}

export default App;
