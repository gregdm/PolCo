'use strict';

angular.module('polcoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('expressionTrans', {
                parent: 'entity',
                url: '/expressionTrans',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.expressionTrans.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expressionTrans/expressionTranss.html',
                        controller: 'ExpressionTransController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expressionTrans');
                        return $translate.refresh();
                    }]
                }
            })
            .state('expressionTransDetail', {
                parent: 'entity',
                url: '/expressionTrans/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'polcoApp.expressionTrans.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/expressionTrans/expressionTrans-detail.html',
                        controller: 'ExpressionTransDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('expressionTrans');
                        return $translate.refresh();
                    }]
                }
            });
    });
