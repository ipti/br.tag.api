<?php

namespace app\modules\registration\controllers;

use yii\web\Controller;
use app\modules\registration\models\Registration;
use app\modules\registration\models\School;
use app\modules\registration\models\Student;
use app\modules\registration\models\Schedule;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use Yii;

class InscriptionController extends Controller
{
    public $enableCsrfValidation = false;
    
    public static function allowedDomains() {
        return [
            '*'
        ];
    }

    public function behaviors()
    {
        return [
            'corsFilter' => [
                'class' => \yii\filters\Cors::className(),
            ],
        ];
    }

    public function actionCreate()
    {
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $data = Yii::$app->request->post();
        $idStudent = "";

        if(empty($data['numRegistration'])){
            $studentData = [
                'Student' => [ 
                    'name' => $data['studentName'],
                    'birthday' => $data['birthday'],
                    'colorRace' => $data['colorRace'],
                    'residenceZone' => $data['residenceZone'],
                    'sex' => $data['sex'],
                    'responsableName' => $data['responsableName'],
                    'fone' => $data['fone'],
                    'schoolInepId' => $data['schoolInepId'],
                    'newStudent' => true
                ]
            ];

            $student = new Student((['scenario' => Registration::SCENARIO_CREATE]));

            if ($student->create($studentData)) {
                $idStudent = (string) $student->_id;
            }
        }else{
            $student = Student::findOne(['studentId' => $data['numRegistration']]);
            $idStudent = (string) $student->_id;
        }

        $registration = new Registration((['scenario' => Registration::SCENARIO_CREATE]));
        
        $dataRegistration = [
            'Registration' => [
                'classroomId' => $data['classroomId'],
                'studentId' => $idStudent
            ]
        ];

        
        if ($registration->create($dataRegistration)) {
            return [
                'status' => '1',
                'data' => ['id' => $registration->registrationNumber],
                'message' => 'Matrícula cadastrada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => ['registration' => 'Matrícula não realizada'],
            'message' => 'Matrícula não realizada'
        ];
        
    }

    

    public function actionSearchschool($search)
    {
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $schools = School::find()->where(["LIKE", "name", $search])->all();
        $data = [];
        foreach ($schools  as $school) {
            $data[] = $school->formatData();
        }

        return $data;
    }

    public function actionSearchstudent($id)
    {
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $student = Student::findOne(['studentId' => $id]);

        if(!is_null($student)){
            return [
                'status' => '1',
                'data' => $student->formatData(),
                'message' => 'Aluno(a) carregado(a) com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => ['student' => 'Aluno(a) não encontrado(a)'],
            'message' => 'Aluno(a) não encontrado(a)'
        ];
        
    }

    public function actionSchool($id)
    {
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $school = School::findOne(new ObjectId($id));
        
        if(!is_null($school)){
            $attributes = $school->getAttributes();
            $attributes['_id'] = (string) $attributes['_id'];
         
            $classrooms = [];
            foreach ($school->classrooms as $classroom) {
                $classroomsAttributes = $classroom->getAttributes();
                $classroomsAttributes['_id'] = (string) $classroomsAttributes['_id'];
                $classroomsAttributes['registrationConfirmed'] = $classroom->registrationConfirmed;
                $classroomsAttributes['registrationRequested'] = $classroom->registrationRequested;
                $classroomsAttributes['registrationRemaining'] = $classroom->registrationRemaining;
                $classroomsAttributes['registrationRefusedCount'] = $classroom->registrationRefusedCount;
                $classrooms[] =  $classroomsAttributes;
            }

            $attributes['classrooms'] = $classrooms;
            return [
                'status' => '1',
                'message' => 'Escola carregada com sucesso',
                'school' => $attributes
            ];
        }

        return [
            'status' => '0',
            'error' => ['school' => 'Escola não encontrada'],
            'message' => 'Escola não encontrada'
        ];
        
    }

    public function actionActive()
    {
        $schedule = Schedule::findOne(['isActive' => true]);
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;

        if (!is_null($schedule)) {
            $data = $schedule->formatData();
            $today = date('Y-m-d'); 
            
            $internalStartDate = date('Y-m-d', strtotime(strtr($data['internalTransferDateStart'], '/', '-'))); 
            $internalEndDate = date('Y-m-d', strtotime(strtr($data['internalTransferDateEnd'], '/', '-'))); 
            $data['internal'] = ($today >= $internalStartDate && $today <= $internalEndDate) ? true : false;

            $newStudentStart = date('Y-m-d', strtotime(strtr($data['newStudentDateStart'], '/', '-'))); 
            $newStudentEnd = date('Y-m-d', strtotime(strtr($data['newStudentDateEnd'], '/', '-'))); 
            $data['newStudent'] = ($today >= $newStudentStart && $today <= $newStudentEnd) ? true : false;
            
            return [
                'status' => '1',
                'data' => $data,
                'message' => 'Cronograma carregado com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $schedule->getErrors(),
            'message' => 'Erro ao excluir cronograma'
        ];
    }
}
?>