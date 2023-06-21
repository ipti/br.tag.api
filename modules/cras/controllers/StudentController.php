<?php

namespace app\modules\cras\controllers;

use app\components\AuthController;
use app\modules\cras\models\Student;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use Yii;

class StudentController extends BaseController
{
    private $pageSize = 12;

    public function actionIndex()
    {
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;

        $students = [];
        $provider = new ActiveDataProvider([
            'query' => Student::find()->where(['type' => 'E', 'stage' => '1', 'classroomSchoolYear'=> date('Y')]),
            'pagination' => [
                'pageSize' => $this->pageSize,
            ]
        ]);

        $pagination = $provider->getPagination();
        $count = $provider->getCount();
        $paginationParam = [];
        if($pagination){
            $paginationParam['currentPage'] = $pagination->page;
            $paginationParam['perPage'] = $pagination->pageSize;
            $paginationParam['totalPages'] = max($pagination->pageCount, 1);
            $paginationParam['totalItens'] = $pagination->totalCount;
        }
        else{
            $paginationParam['currentPage'] = 0;
            $paginationParam['perPage'] = $this->pageSize;
            $paginationParam['totalPages'] = 0;
            $paginationParam['totalItens'] = $count;
        }

        $models = $provider->getModels();

        foreach ($models as $model) {
            $attributes = $model->getAttributes();
            $attributes['_id'] = (string) $attributes['_id'];
            $students[] = $attributes;
        }

        return array_merge(['students' => $students], ['pagination' =>$paginationParam]);
    }

    public function actionView($id)
    {
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $student = Student::findOne(new ObjectId($id));

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

    public function actionSearch()
    {

        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $request = Yii::$app->request->post();
        $data = [];


        if(isset($request['year']) && $request['year'] !== ''){
          $data['classroomSchoolYear'] = "{$request['year']}";
        }

        if(isset($request['situation']) && $request['situation'] !== ''){
          $data['bought'] = $request['situation'];
        }

        if(isset($request['school']) && $request['school'] !== ''){
          $data['schoolInepId'] = $request['school']['value'];
        }

        if(isset($request['classroom']) && $request['classroom'] !== '' ){
          $data['classroomId'] = $request['classroom']['value'];
        }

        $search = Student::find()->where($data);

        $students = [];
        $provider = new ActiveDataProvider([
            'query' =>  $search,
            'pagination' => [
                'pageSize' => $this->pageSize,
            ]
        ]);

        $pagination = $provider->getPagination();
        $count = $provider->getCount();
        $paginationParam = [];
        if($pagination){
            $paginationParam['currentPage'] = $pagination->page;
            $paginationParam['perPage'] = $pagination->pageSize;
            $paginationParam['totalPages'] = max($pagination->pageCount, 1);
            $paginationParam['totalItens'] = $pagination->totalCount;
        }
        else{
            $paginationParam['currentPage'] = 0;
            $paginationParam['perPage'] = $this->pageSize;
            $paginationParam['totalPages'] = 0;
            $paginationParam['totalItens'] = $count;
        }

        $models = $provider->getModels();

        foreach ($models as $model) {
            $attributes = $model->getAttributes();
            $attributes['_id'] = (string) $attributes['_id'];
            $students[] = $attributes;
        }

        return array_merge(['students' => $students], ['pagination' =>$paginationParam]);
    }
}

?>
