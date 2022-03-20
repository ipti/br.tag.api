<?php

namespace app\modules\v1\controllers;

use app\components\AuthController;
use app\modules\v1\models\People;
use MongoDB\BSON\ObjectId;
use MongoDB\BSON\UTCDateTime;
use MongoDB\BSON\Regex;
use yii\data\ActiveDataProvider;
use \DateTime;
use Yii;

class PeopleController extends AuthController
{

    public function actionIndex()
    {

        $data = Yii::$app->request->get();
        $query = People::find();
        $queryCondition = [];

        if(isset($data['name'])){
            $queryCondition['name'] = ['$regex' => $data['name'], '$options' => 'i'];
        }

        if(isset($data['mother'])){
            $queryCondition['mother'] = ['$regex' => $data['mother'], '$options' => 'i'];
        }

        if(isset($data['birthday'])){

            $birthday = DateTime::createFromFormat('d/m/Y', $data['birthday']);
            $birthday->setTime(00, 00, 00);
            $birthday = new UTCDateTime($birthday->getTimeStamp());

            $queryCondition['birthday'] = $birthday;
        }

        if(count($queryCondition) > 0){
            $query->where($queryCondition);
        }
        
        //@TODO CORRIGIR POSTERIORMENTE O METODO DE FIND E AS CONDIÇÕES..TEMPORARIAMENTE DESABILITADOS
        $query = People::find();
        $query->orderBy('name');

        $peoples = [];
        $provider = new ActiveDataProvider([
            'query' => $query,
            'pagination' => [
                'pageSize' => 15,
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
            $paginationParam['perPage'] = 10;
            $paginationParam['totalPages'] = 0;
            $paginationParam['totalItens'] = $count;
        }

        $models = $provider->getModels();

        foreach($models as $model){
            $peoples[] = $model->formatData();
        }
        return array_merge(['peoples' => $peoples], ['pagination' =>$paginationParam]);
    }

    public function actionCreate()
    {
        $people = new People(['scenario' => People::SCENARIO_CREATE]);
        $data['People'] = Yii::$app->request->post();

        if ($people->create($data)) {
            return [
                'status' => '1',
                'payload' => ['_id' => (string) $people->_id],
                'message' => 'Pessoa cadastrada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $people->getErrors(),
            'message' => 'Erro ao cadastrar pessoa'
        ];
    }

    public function actionGet($id){
        $people = People::findOne(new ObjectId($id));
        return $people->formatData();
    }

    public function actionUpdate($id)
    {
        $people = People::findOne(new ObjectId($id));
        $people->scenario = People::SCENARIO_UPDATE;
        $data = ['People' => Yii::$app->request->post()];

        if ($people->_update($data)) {
            return [
                'status' => '1',
                'data' => ['_id' => (string) $people->_id],
                'message' => 'Pessoa atualizada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $people->getErrors(),
            'message' => 'Erro ao atualizar pessoa'
        ];
    }

    public function actionSearch()
    {

        $data = Yii::$app->request->get();
        $query = People::find();
        $query->select(['_id','name','birthday','mother']);
        $queryCondition = [];

        $conditionDate = function($date){
            $date = DateTime::createFromFormat('d/m/Y', $date);
            $date->setTime(00, 00, 00);
            $date = new UTCDateTime($date->getTimeStamp());
            return ['$eq' => $date];
        };

        $conditionLike = function($value){
            return ['$regex' => $value, '$options' => 'i'];
        };

        if(isset($data['fields'])){
            $fields = explode(',', $data['fields']);
            $data = array_map("trim", $fields);

            if(count($data) > 0 && !is_null($data[0])){
                foreach ($data as $field) {
                    if(preg_match("/([012]?[1-9]|[12]0|3[01])\/(0?[1-9]|1[012])\/([0-9]{4})/", $field)){
                        $queryCondition['birthday'] = $conditionDate($field);
                    }
                    else{
                        if(!isset($queryCondition['name'])){
                            $queryCondition['name'] = $conditionLike($field);
                        }
                        else{
                            $queryCondition['mother'] = $conditionLike($field);
                        }
                    }
                }
            }
        }

        if(count($queryCondition) > 0){
            $query->where($queryCondition);
        }
        //@TODO CORRIGIR POSTERIORMENTE O METODO DE FIND E AS CONDIÇÕES..TEMPORARIAMENTE DESABILITADOS
        $query = People::find();
        $peoples = $query->all();

        foreach($peoples as $key => $people){
            $peoples[$key] = $people->formatData();
        }

        return $peoples;
    }

}

