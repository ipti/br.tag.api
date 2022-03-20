<?php

namespace app\modules\v1\controllers;

use app\components\AuthController;
use app\modules\v1\models\Service;
use MongoDB\BSON\ObjectId;
use yii\data\ActiveDataProvider;
use Yii;

class ServiceController extends AuthController
{

    public function actionIndex()
    {

        $data = Yii::$app->request->get();
        $query = Service::find();
        $queryCondition = [];

        if(isset($data['createAt'])){

            $createAt = date('Y-m-d', strtotime($data['createAt']));
            $createAt = new DateTime($createAt);
            $createAt = new UTCDateTime($createAt->getTimeStamp());

            $queryCondition['createAt'] = $createAt;
        }

        if(count($queryCondition) > 0){
            $query->where($queryCondition);
        }

        //$query->orderBy('createAt');

        $services = [];
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
            $services[] = $model->formatData();
        }
        return array_merge(['services' => $services], ['pagination' =>$paginationParam]);
    }

    public function actionCreate()
    {
        $service = new Service(['scenario' => Service::SCENARIO_CREATE]);
        $data['Service'] = Yii::$app->request->post();

        if ($service->create($data)) {
            return [
                'status' => '1',
                'payload' => ['_id' => (string) $service->_id],
                'message' => 'Requisição de serviço cadastrada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $service->getErrors(),
            'message' => 'Erro ao cadastrar requisição de serviço'
        ];
    }

    public function actionGet($id){
        $service = Service::findOne($id);
        if(!is_null($service)){
            return $service->formatData();
        }
        return [];
    }

    public function actionUpdate($id)
    {
        $service = Service::findOne(new ObjectId($id));
        $service->scenario = Service::SCENARIO_UPDATE;
        $data = ['Service' => Yii::$app->request->post()];

        if ($service->_update($data)) {
            return [
                'status' => '1',
                'data' => ['_id' => (string) $service->_id],
                'message' => 'Requisição de serviço atualizada com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $service->getErrors(),
            'message' => 'Erro ao atualizar requisição de serviço'
        ];
    }

    public function actionDelete($id)
    {
        $service = Service::findOne(new ObjectId($id));

        if (!is_null($service) && $service->delete()) {
            return [
                'status' => '1',
                'message' => 'Requisição de serviço excluída com sucesso'
            ];
        }

        return [
            'status' => '0',
            'error' => $service->getErrors(),
            'message' => 'Erro ao atualizar requisição de serviço'
        ];
    }

}

